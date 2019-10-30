package com.appsstuff.pharma.service;

import java.util.HashSet;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.appsstuff.pharma.dto.req.RegistrationForm;
import com.appsstuff.pharma.enums.RoleType;
import com.appsstuff.pharma.exception.custom.EmailAlreadyExistException;
import com.appsstuff.pharma.repo.ConfirmationTokenRepository;
import com.appsstuff.pharma.repo.RoleRepository;
import com.appsstuff.pharma.repo.UserRepository;
import com.appsstuff.pharma.security.model.ConfirmationToken;
import com.appsstuff.pharma.security.model.Role;
import com.appsstuff.pharma.security.model.User;

@Service
public class RegistrationService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserRepository userRepo;
	@Autowired
	private RoleRepository roleRepo;
	@Autowired
	private ConfirmationTokenRepository confirmationTokenRepository;
	@Autowired
	private EmailSenderService emailSenderService;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Value("${mail.confirmation.url}")
	private String confirmationUrl;
	@Value("${mail.confirmation.subject}")
	private String confirmationSubject;
	@Value("${mail.confirmation.message}")
	private String confirmationMailMessage;

	/**
	 * This method to add new user to the system by specific email and encoded
	 * password
	 * 
	 * @param email
	 * @param password
	 * @param roleType
	 * @throws EmailAlreadyExistException
	 */

	@Transactional // doit as a one transaction to rollback in case failure
	public void saveUser(RegistrationForm registrationForm, RoleType roleType) throws EmailAlreadyExistException {
		logger.info("Start of saveUser");

		isEmailExist(registrationForm.getEmail());

		User user = new User(registrationForm.getEmail(), encodePassword(registrationForm.getPassword()));

		HashSet<Role> roles = new HashSet<>();

		roles.add(roleRepo.findByName(roleType.name()));
		user.setRoles(roles);

		if (!roleType.equals(RoleType.ROLE_SUPER)) {

			sendConfirmationMail(user);

		} else {
			user.setEnabled(true);
		}

		userRepo.save(user);

		logger.info("End of saveUser");

	}

	/**
	 * 
	 * @param user
	 */
	private void sendConfirmationMail(User user) {
		ConfirmationToken confirmationToken = new ConfirmationToken(user);

		// sending confirmation email
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(user.getEmail());
		mailMessage.setSubject(confirmationUrl);
		mailMessage
				.setText(confirmationMailMessage + "\n" + confirmationUrl + confirmationToken.getConfirmationToken());

		emailSenderService.sendEmail(mailMessage);

		confirmationTokenRepository.save(confirmationToken);

	}

	/**
	 * 
	 * @param password
	 * @return
	 */
	private String encodePassword(String password) {
		return passwordEncoder.encode(password);

	}

	/**
	 * check if email is already registered before
	 * 
	 * @param email
	 * @return
	 * @throws EmailAlreadyExistException
	 */
	private void isEmailExist(String email) throws EmailAlreadyExistException {
		logger.info("Start of isEmailExist ");

		if (userRepo.findByEmail(email) != null)
			throw new EmailAlreadyExistException("Provided Email Already Regestired !");

		logger.info("End of isEmailExist");
	}

}