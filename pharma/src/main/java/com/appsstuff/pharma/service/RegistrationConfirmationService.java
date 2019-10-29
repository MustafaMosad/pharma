package com.appsstuff.pharma.service;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appsstuff.pharma.exception.custom.RegisterationConfirmationTokenNotExist;
import com.appsstuff.pharma.repo.ConfirmationTokenRepository;
import com.appsstuff.pharma.repo.UserRepository;
import com.appsstuff.pharma.security.model.ConfirmationToken;
import com.appsstuff.pharma.security.model.User;

@Service
public class RegistrationConfirmationService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired // inject user repository to access and perform queries on User entity
	private UserRepository userRepo;
	@Autowired // inject ConfirmationToken repository to access and perform queries on
				// ConfirmationToken entity
	private ConfirmationTokenRepository confirmationTokenRepository;

	/**
	 * This method used to activate user account by setting its is enabled flag by
	 * true
	 * 
	 * @param token
	 * @throws RegisterationConfirmationTokenNotExist
	 */
	public void activateUseraccount(String token) throws RegisterationConfirmationTokenNotExist {
		logger.info("Start Of activateUseraccount");
		logger.debug("confirmationToken is :" + token);

		ConfirmationToken confirmationtoken = confirmationTokenRepository.findByConfirmationToken(token);

		if (confirmationtoken == null)
			throw new RegisterationConfirmationTokenNotExist();

		logger.debug("confirmationToken found successfully");

		User user = confirmationtoken.getUser();
		Objects.requireNonNull(user);
		logger.debug("User of provided confirmationToken found successfully : " + user.getEmail());
		// enable user account
		user.setEnabled(true);
		userRepo.save(user);
		logger.info("End Of activateUseraccount");

	}
}
