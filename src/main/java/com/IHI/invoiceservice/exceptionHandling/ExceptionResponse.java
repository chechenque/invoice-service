package com.IHI.invoiceservice.exceptionHandling;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ExceptionResponse {

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="dd-MM-yyyy hh:mm:ss")
	private LocalDateTime timeStamp;
	private Integer status;
	private HttpStatus error;
	private String message;
	private String path;
	
	public ExceptionResponse() {
		this.timeStamp = LocalDateTime.now();
	}

	public ExceptionResponse(LocalDateTime timeStamp, Integer status, HttpStatus error, String message, String path) {
		super();
		this.timeStamp = timeStamp;
		this.status = status;
		this.error = error;
		this.message = message;
		this.path = path;
	}

	public LocalDateTime getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(LocalDateTime timeStamp) {
		this.timeStamp = timeStamp;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public HttpStatus getError() {
		return error;
	}

	public void setError(HttpStatus error) {
		this.error = error;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
