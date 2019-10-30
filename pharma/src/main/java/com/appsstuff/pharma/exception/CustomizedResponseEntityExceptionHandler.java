package com.appsstuff.pharma.exception;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.appsstuff.pharma.exception.custom.AuthenticationException;
import com.appsstuff.pharma.exception.custom.EmailAlreadyExistException;
import com.appsstuff.pharma.exception.custom.EmailNotFoundException;
import com.appsstuff.pharma.exception.custom.RegisterationConfirmationTokenNotExist;
import com.appsstuff.pharma.exception.custom.ResetTokenExpiredException;
import com.appsstuff.pharma.exception.custom.ResetTokenNotFoundException;
import com.appsstuff.pharma.exception.model.ExceptionResponse;

@ControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * handle syntax errors of rest calls
	 */
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		logger.info("Start of handleMethodArgumentNotValid");
		List<String> messages = new ArrayList<String>();

		messages.add("Syntax Error");
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), HttpStatus.BAD_REQUEST.value(),
				HttpStatus.BAD_REQUEST.name(), messages);
		logger.info("End of handleHttpMessageNotReadable");

		return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}

	/**
	 * handle javax validations exception and build error response object with
	 * validation messages getting all error messages and add it to Exceptions
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		logger.info("Start of handleMethodArgumentNotValid");
		List<String> messages = new ArrayList<String>();

		for (ObjectError error : ex.getBindingResult().getAllErrors()) {
			messages.add(error.getDefaultMessage());
		}
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), HttpStatus.BAD_REQUEST.value(),
				HttpStatus.BAD_REQUEST.name(), messages);
		logger.info("End of handleMethodArgumentNotValid");

		return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<ExceptionResponse> handleAuthenticationError(AuthenticationException ex) {

		logger.info("Start of handleAuthenticationError");
		List<String> messages = new ArrayList<String>();
		messages.add(ex.getMessage());

		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), HttpStatus.UNAUTHORIZED.value(),
				HttpStatus.UNAUTHORIZED.name(), messages);
		logger.info("End of handleAuthenticationError");

		return new ResponseEntity<>(exceptionResponse, HttpStatus.UNAUTHORIZED);

	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ExceptionResponse> handleAccessDenied(AccessDeniedException ex) {

		logger.info("Start of handleAccessDenied");
		List<String> messages = new ArrayList<String>();
		messages.add("You are not authorized to access this resource");

		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), HttpStatus.FORBIDDEN.value(),
				HttpStatus.FORBIDDEN.name(), messages);
		logger.info("End of handleAccessDenied");

		return new ResponseEntity<>(exceptionResponse, HttpStatus.FORBIDDEN);

	}

	@ExceptionHandler(RegisterationConfirmationTokenNotExist.class)
	public ResponseEntity<ExceptionResponse> handleRegisterationConfirmationTokenNotExist(
			RegisterationConfirmationTokenNotExist ex) {

		logger.info("Start of RegisterationConfirmationTokenNotExist");
		List<String> messages = new ArrayList<String>();
		messages.add(ex.getErrorMessage());

		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), HttpStatus.NOT_FOUND.value(),
				HttpStatus.NOT_FOUND.name(), messages);
		logger.info("End of RegisterationConfirmationTokenNotExist");

		return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);

	}

	@ExceptionHandler(ResetTokenNotFoundException.class)
	public ResponseEntity<ExceptionResponse> handleResetTokenNotFoundException(ResetTokenNotFoundException ex) {

		logger.info("Start of ResetTokenNotFoundException");
		List<String> messages = new ArrayList<String>();
		messages.add(ex.getErrorMessage());

		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), HttpStatus.NOT_FOUND.value(),
				HttpStatus.NOT_FOUND.name(), messages);
		logger.info("End of ResetTokenNotFoundException");

		return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);

	}

	@ExceptionHandler(EmailNotFoundException.class)
	public ResponseEntity<ExceptionResponse> handleEmailNotFoundException(EmailNotFoundException ex) {

		logger.info("Start of EmailNotFoundException");
		List<String> messages = new ArrayList<String>();
		messages.add(ex.getErrorMessage());

		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), HttpStatus.NOT_FOUND.value(),
				HttpStatus.NOT_FOUND.name(), messages);
		logger.info("End of EmailNotFoundException");

		return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);

	}

	@ExceptionHandler(ResetTokenExpiredException.class)
	public ResponseEntity<ExceptionResponse> handleResetTokenExpiredException(ResetTokenExpiredException ex) {

		logger.info("Start of ResetTokenExpiredException");
		List<String> messages = new ArrayList<String>();
		messages.add(ex.getErrorMessage());

		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), HttpStatus.BAD_REQUEST.value(),
				HttpStatus.BAD_REQUEST.name(), messages);
		logger.info("End of ResetTokenExpiredException");

		return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);

	}

	@ExceptionHandler(EmailAlreadyExistException.class)
	public ResponseEntity<ExceptionResponse> handleEmailAlreadyExistException(EmailAlreadyExistException ex) {

		logger.info("Start of EmailAlreadyExistException");
		List<String> messages = new ArrayList<String>();
		messages.add(ex.getErrorMessage());

		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), HttpStatus.CONFLICT.value(),
				HttpStatus.CONFLICT.name(), messages);
		logger.info("End of EmailAlreadyExistException");

		return new ResponseEntity<>(exceptionResponse, HttpStatus.CONFLICT);

	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ExceptionResponse> handleUnknownException(Exception ex, WebRequest request) {

		logger.info("Start of handleUnknownException");
		List<String> messages = new ArrayList<String>();
		messages.add("Unexpected Error !");
		logger.debug(ex.getMessage());
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(),
				HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.name(), messages);
		logger.info("End of handleUnknownException");

		return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);

	}
}