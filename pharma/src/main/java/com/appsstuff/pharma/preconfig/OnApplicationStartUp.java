package com.appsstuff.pharma.preconfig;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.appsstuff.pharma.dto.RegistrationForm;
import com.appsstuff.pharma.enums.RoleType;
import com.appsstuff.pharma.exception.custom.EmailAlreadyExistException;
import com.appsstuff.pharma.repo.RoleRepository;
import com.appsstuff.pharma.security.model.Role;
import com.appsstuff.pharma.service.RegistrationService;

@Component
public class OnApplicationStartUp {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private RegistrationService registrationService;

	@Autowired
	private RoleRepository roleRepo;

	@EventListener
	@Transactional
	public void onApplicationEvent(ContextRefreshedEvent event) {
		logger.info("Start Of onApplicationEvent");
		fillRoleTable();
		try {
			createSuperUser();
		} catch (EmailAlreadyExistException e) {
			logger.warn("Super User Already Exist in DB");
			// eating the exception ,, this means Super user Already Created and exist in
			// DB.
		}
		logger.info("End Of onApplicationEvent");

	}

	/**
	 * @throws EmailAlreadyExistException
	 * @throws StudentAlreadyExistException
	 * 
	 */
	private void createSuperUser() throws EmailAlreadyExistException {

		RegistrationForm registrationForm = new RegistrationForm();
		registrationForm.setEmail("admin@gmail.com");
		registrationForm.setPassword("admin123");

		registrationService.saveUser(registrationForm, RoleType.ROLE_SUPER);
	}

	/**
	 * 
	 */
	private void fillRoleTable() {
		logger.info("Start Of fillRoleTable");

		List<Role> roles = roleRepo.findAll();

		if (roles == null || roles.isEmpty()) {
			logger.debug("No Roles In a table ");
			roleRepo.save(new Role(RoleType.ROLE_USER.name()));
			logger.debug("Role .. ROLE_USER inserted");
			roleRepo.save(new Role(RoleType.ROLE_ADMIN.name()));
			logger.debug("Role .. ROLE_ADMIN inserted");
			roleRepo.save(new Role(RoleType.ROLE_SUPER.name()));
			logger.debug("Role .. ROLE_SUPER inserted");

		}
		logger.info("End Of fillRoleTable");

	}

}