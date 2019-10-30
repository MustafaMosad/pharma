package com.appsstuff.pharma.dto.req;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class ResetPasswordForm {

	@NotEmpty(message = "password can not be empty")
	@Pattern(regexp = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%!^*-_]).{8,30})", message = " password must be at least 8 characters, at most 30 , contains 1 uppercase character"
			+ ", contains 1 lowercase character , contains 1 special character from @#$%!^*-_" + "and 1 number")
	private String password;
	@NotEmpty(message = "confirm password cannot be empty")
	private String confirmPassword;

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
