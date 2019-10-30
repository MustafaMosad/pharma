package com.appsstuff.pharma.controller;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.appsstuff.pharma.exception.custom.AuthenticationException;
import com.appsstuff.pharma.security.dto.req.JwtTokenRequest;
import com.appsstuff.pharma.security.dto.res.JwtTokenResponse;
import com.appsstuff.pharma.security.service.JwtUserDetailsService;
import com.appsstuff.pharma.security.util.JwtTokenUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/user")
@Api(value = "Login Controller", description = "This Controller contains APIs for Authnticate User and generate token.")
public class LoginController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserDetailsService jwtUserDetailsService;

	@RequestMapping(value = "${jwt.get.token.uri}", method = RequestMethod.POST)
	@ApiOperation(value = "Login to the system as super , admin or regular user", response = JwtTokenResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully logged in and token generated"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
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
