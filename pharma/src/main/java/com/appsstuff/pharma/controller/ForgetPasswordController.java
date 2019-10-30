package com.appsstuff.pharma.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.appsstuff.pharma.dto.req.ForgetPasswordForm;
import com.appsstuff.pharma.dto.req.ResetPasswordForm;
import com.appsstuff.pharma.exception.custom.EmailNotFoundException;
import com.appsstuff.pharma.exception.custom.ResetTokenExpiredException;
import com.appsstuff.pharma.exception.custom.ResetTokenNotFoundException;
import com.appsstuff.pharma.service.ForgetPasswordService;
import com.appsstuff.pharma.service.ResetPasswordService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/user/forget-password")
@Api(value = "Forget Password Controller", description = "This Controller contains APIs for forget and reset password feauters")
public class ForgetPasswordController {

	@Autowired
	private ForgetPasswordService forgetPasswordService;
	@Autowired
	private ResetPasswordService resetPasswordService;

	@PostMapping
	public ResponseEntity<?> processForgotPassword(@RequestBody @Valid ForgetPasswordForm forgetPasswordForm)
			throws EmailNotFoundException {

		forgetPasswordService.processForgetPasswordReq(forgetPasswordForm);
		return ResponseEntity.ok().build();

	}

	@PostMapping(value = "/reset/{resetToken}")
	public ResponseEntity<?> resetPassword(@RequestParam String resetToken,
			@RequestBody @Valid ResetPasswordForm resetPasswordForm)
			throws ResetTokenNotFoundException, ResetTokenExpiredException {
		resetPasswordService.resetPassword(resetToken, resetPasswordForm);
		return ResponseEntity.ok().build();
	}
}
