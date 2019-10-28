package com.appsstuff.pharma.security.filter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.Three2one.elearning.exception.model.ExceptionResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@Order(1)
public class ExceptionHandlerFilter extends OncePerRequestFilter {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			filterChain.doFilter(request, response);
		} catch (RuntimeException e) {

			logger.info("Start of handleAuthenticationError");
			List<String> messages = new ArrayList<String>();
			messages.add("You would need to provide a valid Jwt Token to Access This resource");

			ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), HttpStatus.UNAUTHORIZED.value(),
					HttpStatus.UNAUTHORIZED.name(), messages);

			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			response.setContentType("application/json");
			response.getWriter().write(convertObjectToJson(exceptionResponse));

			logger.info("End of handleAuthenticationError {} ", response.toString());

		}
	}

	public String convertObjectToJson(Object object) throws JsonProcessingException {
		if (object == null) {
			return null;
		}
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(object);
	}
}