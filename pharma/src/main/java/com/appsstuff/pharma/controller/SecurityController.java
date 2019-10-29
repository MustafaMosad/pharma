package com.appsstuff.pharma.controller;

import java.util.Objects;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.appsstuff.pharma.dto.RegistrationForm;
import com.appsstuff.pharma.enums.RoleType;
import com.appsstuff.pharma.exception.custom.AuthenticationException;
import com.appsstuff.pharma.exception.custom.EmailAlreadyExistException;
import com.appsstuff.pharma.exception.custom.RegisterationConfirmationTokenNotExist;
import com.appsstuff.pharma.security.dto.req.JwtTokenRequest;
import com.appsstuff.pharma.security.dto.res.JwtTokenResponse;
import com.appsstuff.pharma.security.service.JwtUserDetailsService;
import com.appsstuff.pharma.security.util.JwtTokenUtil;
import com.appsstuff.pharma.service.RegistrationConfirmationService;
import com.appsstuff.pharma.service.RegistrationService;

@RestController
@RequestMapping("/api")
public class SecurityController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Value("${jwt.http.request.header}")
	private String tokenHeader;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserDetailsService jwtUserDetailsService;
	@Autowired
	private RegistrationService registrationService;

	@Autowired
	private RegistrationConfirmationService registrationConfirmationService;

	@RequestMapping(value = "/registration-user", method = RequestMethod.POST)
	public ResponseEntity<?> registerUser(@RequestBody @Valid RegistrationForm registrationForm)
			throws EmailAlreadyExistException {

		registrationService.saveUser(registrationForm, RoleType.ROLE_USER);
		return ResponseEntity.ok().build();
	}

	@RequestMapping(value = "/registration-admin", method = RequestMethod.POST)
	@PreAuthorize("hasRole('ROLE_SUPER')")
	public ResponseEntity<?> registerAdmin(@RequestBody @Valid RegistrationForm registrationForm)
			throws EmailAlreadyExistException {

		registrationService.saveUser(registrationForm, RoleType.ROLE_ADMIN);
		return ResponseEntity.ok().build();
	}

	@RequestMapping(value = "/confirm-registration/{accountConfirmationToken}", method = RequestMethod.POST)
	public ResponseEntity<?> confirmRegistration(@RequestParam String accountConfirmationToken)
			throws RegisterationConfirmationTokenNotExist {

		logger.info(accountConfirmationToken);
		registrationConfirmationService.activateUseraccount(accountConfirmationToken);

		return ResponseEntity.ok().build();

	}

	@RequestMapping(value = "${jwt.get.token.uri}", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtTokenRequest authenticationRequest)
			throws AuthenticationException {

		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

		final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());

		if (!((JwtUserDetailsService) jwtUserDetailsService).isUserVerfied(authenticationRequest.getUsername())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		final String token = jwtTokenUtil.generateToken(userDetails);

		return ResponseEntity.ok(new JwtTokenResponse(token));
	}

	/**
	 * 
	 * @param username
	 * @param password
	 */
	private void authenticate(String username, String password) {
		Objects.requireNonNull(username);
		Objects.requireNonNull(password);

		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new AuthenticationException("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new AuthenticationException("INVALID_CREDENTIALS", e);
		}
	}
}