package com.appsstuff.pharma.dto.req;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class RegistrationForm {
	@NotEmpty(message = "email can not be empty")
	@Email(message = " Please provide valid Email")
	private String email;
	@NotEmpty(message = "password can not be empty")
	@Pattern(regexp = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%!^*-_]).{8,30})", message = " password must be at least 8 characters, at most 30 , contains 1 uppercase character"
			+ ", contains 1 lowercase character , contains 1 special character from @#$%!^*-_" + "and 1 number")
	private String password;
	@NotEmpty(message = "confirm password cannot be empty")
	private String confirmPassword;

	@AssertTrue(message = "confirm password field should be equal to password field")
	@JsonIgnore
	public boolean isValid() {
		return this.password.equals(this.confirmPassword);
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

}