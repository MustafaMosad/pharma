package com.appsstuff.pharma.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.appsstuff.pharma.dto.req.ResetPasswordForm;
import com.appsstuff.pharma.exception.custom.ResetTokenExpiredException;
import com.appsstuff.pharma.exception.custom.ResetTokenNotFoundException;
import com.appsstuff.pharma.repo.PasswordResetTokenRepository;
import com.appsstuff.pharma.repo.UserRepository;
import com.appsstuff.pharma.security.model.PasswordResetToken;
import com.appsstuff.pharma.security.model.User;

@Service
public class ResetPasswordService {

	@Autowired
	private UserRepository userRepo;
	@Autowired
	private PasswordResetTokenRepository passwordResetTokenRepo;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	/**
	 * 
	 * @param token
	 * @param resetPasswordForm
	 * @throws ResetTokenNotFoundException
	 * @throws ResetTokenExpiredException
	 */
	public void resetPassword(String token, ResetPasswordForm resetPasswordForm)
			throws ResetTokenNotFoundException, ResetTokenExpiredException {

		PasswordResetToken passwordResetToken;

		if (token == null || (passwordResetToken = passwordResetTokenRepo.findByToken(token)) == null)
			throw new ResetTokenNotFoundException("Password Reset Token Not Found");

		if (passwordResetToken.isExpired())
			throw new ResetTokenExpiredException("Password Reset Token is Expired !");

		User user = passwordResetToken.getUser();

		user.setPassword(encodePassword(resetPasswordForm.getPassword()));
		userRepo.save(user);
		passwordResetTokenRepo.delete(passwordResetToken);

	}

	/**
	 * 
	 * @param password
	 * @return
	 */
	private String encodePassword(String password) {
		return passwordEncoder.encode(password);

	}
}
