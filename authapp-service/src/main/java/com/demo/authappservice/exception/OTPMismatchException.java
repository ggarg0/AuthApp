package com.demo.authappservice.exception;

public class OTPMismatchException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public OTPMismatchException() {
	}

	public OTPMismatchException(String message) {
		super(message);
	}

	public OTPMismatchException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
