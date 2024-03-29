package com.demo.authappservice.exception;

public class InvalidRequestException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public InvalidRequestException() {
	}

	public InvalidRequestException(String message) {
		super(message);
	}

	public InvalidRequestException(String message, Throwable throwable) {
		super(message, throwable);
	}
}