package com.appsstuff.pharma.logging.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.appsstuff.pharma.logging.filter.Slf4jMDCFilter;

@Configuration
public class Slf4jMDCFilterConfiguration {

	public static final String DEFAULT_RESPONSE_TOKEN_HEADER = "Request_ID";
	public static final String DEFAULT_MDC_UUID_TOKEN_KEY = "Slf4jMDCFilter.UUID";
	public static final String DEFAULT_MDC_CLIENT_IP_KEY = "Slf4jMDCFilter.ClientIP";
	public static final String DEFAULT_MDC_CLIENT_USER_NAME = "Slf4jMDCFilter.Username";
	private String mdcClientUsername = DEFAULT_MDC_CLIENT_USER_NAME;

	@Autowired
	private Slf4jMDCFilter log4jMDCFilterFilter;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Bean
	public FilterRegistrationBean servletRegistrationBean() {
		final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		log4jMDCFilterFilter.setMdcTokenKey(DEFAULT_MDC_UUID_TOKEN_KEY);
		log4jMDCFilterFilter.setMdcClientIpKey(DEFAULT_MDC_CLIENT_IP_KEY);
		log4jMDCFilterFilter.setMdcClientUsername(mdcClientUsername);

		registrationBean.setFilter(log4jMDCFilterFilter);
		registrationBean.setOrder(2);
		return registrationBean;
	}
}