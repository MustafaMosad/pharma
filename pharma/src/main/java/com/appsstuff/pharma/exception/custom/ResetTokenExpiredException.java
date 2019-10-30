package com.appsstuff.pharma.exception.custom;

public class ResetTokenExpiredException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String errorMessage;

	public ResetTokenExpiredException() {
		super();
	}

	public ResetTokenExpiredException(String errorMessage) {
		super(errorMessage);
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
