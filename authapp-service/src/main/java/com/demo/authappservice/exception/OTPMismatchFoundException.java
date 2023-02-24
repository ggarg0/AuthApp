package com.demo.authappservice.exception;

public class OTPMismatchFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public OTPMismatchFoundException() {
	}

	public OTPMismatchFoundException(String message) {
		super(message);
	}

	public OTPMismatchFoundException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
