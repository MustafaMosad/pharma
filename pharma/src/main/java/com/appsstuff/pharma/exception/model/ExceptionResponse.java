package com.appsstuff.pharma.exception.model;

import java.util.Date;
import java.util.List;

public class ExceptionResponse {

	private Date timestamp;
	private Integer status;
	private String error;
	private List<String> messages;

	public ExceptionResponse(Date timestamp, Integer status, String error, List<String> messages) {
		super();
		this.timestamp = timestamp;
		this.status = status;
		this.error = error;
		this.messages = messages;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public List<String> getMessages() {
		return messages;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

}
