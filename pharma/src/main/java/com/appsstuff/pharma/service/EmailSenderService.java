package com.appsstuff.pharma.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service("emailSenderService")
public class EmailSenderService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private JavaMailSender javaMailSender;

	@Autowired
	public EmailSenderService(JavaMailSender javaMailSender) {
		logger.info("Start Of EmailSenderService Constructor");
		this.javaMailSender = javaMailSender;
		logger.info("End Of EmailSenderService Constructor");

	}

	@Async
	public void sendEmail(SimpleMailMessage email) {
		logger.info("Start Of sendEmail");
		javaMailSender.send(email);
		logger.info("End Of sendEmail");

	}
}