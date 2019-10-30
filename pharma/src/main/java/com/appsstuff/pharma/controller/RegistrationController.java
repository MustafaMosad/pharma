package com.appsstuff.pharma.controller;

import java.net.URI;
import java.net.URISyntaxException;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.appsstuff.pharma.dto.req.RegistrationForm;
import com.appsstuff.pharma.enums.RoleType;
import com.appsstuff.pharma.exception.custom.EmailAlreadyExistException;
import com.appsstuff.pharma.exception.custom.RegisterationConfirmationTokenNotExist;
import com.appsstuff.pharma.service.RegistrationConfirmationService;
import com.appsstuff.pharma.service.RegistrationService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/user/registration")
@Api(value = "Registration Controller", description = "This Controller contains APIs for Register Admin and regular user ")
public class RegistrationController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Value("${jwt.http.request.header}")
	private String tokenHeader;

	@Value("${jwt.get.token.uri}")
	private String authenticationPath;

	@Autowired
	private RegistrationService registrationService;

	@Autowired
	private RegistrationConfirmationService registrationConfirmationService;

	@RequestMapping(value = "/regular", method = RequestMethod.POST)
	@ApiOperation(value = "Register new User as A Reqular user")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Successfully Regular user Added and waiting for confirmation by email"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
	public ResponseEntity<?> registerUser(@RequestBody @Valid RegistrationForm registrationForm)
			throws EmailAlreadyExistException, URISyntaxException {

		registrationService.saveUser(registrationForm, RoleType.ROLE_USER);
		return ResponseEntity.created(new URI(authenticationPath)).build();
	}

	@PreAuthorize("hasRole('ROLE_SUPER')")
	@RequestMapping(value = "/admin", method = RequestMethod.POST)
	@ApiOperation(value = "Register new User as An Admin user")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Successfully Admin user Added and waiting for confirmation by email"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
	public ResponseEntity<?> registerAdmin(@RequestBody @Valid RegistrationForm registrationForm)
			throws EmailAlreadyExistException, URISyntaxException {

		registrationService.saveUser(registrationForm, RoleType.ROLE_ADMIN);
		return ResponseEntity.created(new URI(authenticationPath)).build();
	}

	@ApiOperation(value = "Login to the system as super , admin or regular user")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully user registration confirmed and he can now log in ."),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The Provided confirmation token is not valid !") })
	@RequestMapping(value = "/confirm-registration/{accountConfirmationToken}", method = RequestMethod.POST)
	public ResponseEntity<?> confirmRegistration(@RequestParam String accountConfirmationToken)
			throws RegisterationConfirmationTokenNotExist {

		logger.info(accountConfirmationToken);
		registrationConfirmationService.activateUseraccount(accountConfirmationToken);

		return ResponseEntity.ok().build();

	}
}