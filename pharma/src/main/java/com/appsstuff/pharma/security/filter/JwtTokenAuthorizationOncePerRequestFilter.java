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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.filter.OncePerRequestFilter;

import com.Three2one.elearning.exception.custom.AuthenticationException;
import com.Three2one.elearning.exception.model.ExceptionResponse;
import com.Three2one.elearning.security.util.JwtTokenUtil;

import io.jsonwebtoken.ExpiredJwtException;

@Component
@Order(2)
public class JwtTokenAuthorizationOncePerRequestFilter extends OncePerRequestFilter {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserDetailsService jwtUserDetailsService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Value("${jwt.http.request.header}")
	private String tokenHeader;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		logger.debug("Authentication Request For '{}'", request.getRequestURL());
		final String requestTokenHeader = request.getHeader(this.tokenHeader);

		String username = null;
		String jwtToken = null;
		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			jwtToken = requestTokenHeader.substring(7);
			try {
				username = jwtTokenUtil.getUsernameFromToken(jwtToken);
			} catch (IllegalArgumentException e) {
				logger.error("JWT_TOKEN_UNABLE_TO_GET_USERNAME", e);
			} catch (ExpiredJwtException e) {
				logger.warn("JWT_TOKEN_EXPIRED", e);
			} catch (Exception e) {
				logger.error("JWT_Exception", e.getMessage());
				throw new AuthenticationException("INVALID_CREDENTIALS");
			}
		} else {
			logger.warn("JWT_TOKEN_DOES_NOT_START_WITH_BEARER_STRING");
		}

		// if (username == null)
		// throw new AuthenticationException("INVALID_CREDENTIALS");

		logger.debug("JWT_TOKEN_USERNAME_VALUE '{}'", username);
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

			UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);

			if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}
		chain.doFilter(request, response);
	}

	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<ExceptionResponse> handleAuthenticationError(AuthenticationException ex) {

		logger.info("Start of handleAuthenticationError");
		List<String> messages = new ArrayList<String>();
		messages.add(ex.getMessage());

		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), HttpStatus.UNAUTHORIZED.value(),
				HttpStatus.UNAUTHORIZED.name(), messages);
		logger.info("End of handleAuthenticationError");

		return new ResponseEntity<>(exceptionResponse, HttpStatus.UNAUTHORIZED);

	}
}
