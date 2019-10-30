package com.appsstuff.pharma.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import com.appsstuff.pharma.dto.req.ForgetPasswordForm;
import com.appsstuff.pharma.exception.custom.EmailNotFoundException;
import com.appsstuff.pharma.repo.PasswordResetTokenRepository;
import com.appsstuff.pharma.repo.UserRepository;
import com.appsstuff.pharma.security.model.PasswordResetToken;
import com.appsstuff.pharma.security.model.User;

@Service
public class ForgetPasswordService {

	@Autowired
	private UserRepository userRepo;
	@Autowired
	private PasswordResetTokenRepository passwordResetTokenRepo;
	@Autowired
	private EmailSenderService emailSenderService;

	@Value("${mail.resetPassword.url}")
	private String resetPasswordUrl;
	@Value("${mail.resetPassword.subject}")
	private String resetPasswordSubject;
	@Value("${mail.resetPassword.message}")
	private String resetPasswordMailMessage;

	/**
	 * 
	 * @param forgetPasswordForm
	 * @throws EmailNotFoundException
	 */
	public void processForgetPasswordReq(ForgetPasswordForm forgetPasswordForm) throws EmailNotFoundException {

		User user = findUserByEmail(forgetPasswordForm.getEmail());
		if (user == null)
			throw new EmailNotFoundException("provided email not found !");

		String resetTokent = generateResetToken(user);

		sendResetPasswordMail(resetTokent, forgetPasswordForm.getEmail());

	}

	/**
	 * 
	 * @param user
	 */
	private String generateResetToken(User user) {
		PasswordResetToken token = new PasswordResetToken();
		token.setToken(UUID.randomUUID().toString());
		token.setUser(user);
		// in minutes
		token.setExpiryDate(60);
		passwordResetTokenRepo.save(token);

		return token.getToken();

	}

	/**
	 * 
	 * @param email
	 */
	private User findUserByEmail(String email) {

		return userRepo.findByEmail(email);

	}

	/**
	 * 
	 * @param user
	 */
	private void sendResetPasswordMail(String resetToken, String userEmail) {

		// sending confirmation email
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(userEmail);
		mailMessage.setSubject(resetPasswordUrl);
		mailMessage.setText(resetPasswordMailMessage + "\n" + resetPasswordUrl + resetToken);

		emailSenderService.sendEmail(mailMessage);

	}
}
