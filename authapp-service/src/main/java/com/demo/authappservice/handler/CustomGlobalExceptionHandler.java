package com.demo.authappservice.handler;

import java.io.IOException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.demo.authappservice.exception.InvalidRequestException;
import com.demo.authappservice.exception.OTPMismatchException;
import com.demo.authappservice.exception.ResourceNotFoundException;
import com.demo.authappservice.exception.UserNotFoundException;
import com.demo.authappservice.formatter.APIError;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletResponse;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class CustomGlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletResponse response) {
		System.out.println("handleMethodArgumentNotValid called");
		String error = "Invalid request parameters/headers passed";
		return buildResponseEntity(new APIError(HttpStatus.BAD_REQUEST, error, ex));
	}

	@ExceptionHandler(InvalidRequestException.class)
	public ResponseEntity<Object> handleInvalidRequestException(InvalidRequestException ex, HttpServletResponse response) {
		System.out.println("handleInvalidRequestException called");
		String error = "Invalid request parameters/headers passed";
		return buildResponseEntity(new APIError(HttpStatus.BAD_REQUEST, error, ex));
	}

	@ExceptionHandler(OTPMismatchException.class)
	public ResponseEntity<Object> hanldeOTPMismatchException(OTPMismatchException ex, HttpServletResponse response) throws IOException {
		System.out.println("hanldeOTPMismatchException called");
		String error = "OTP mismatch found";
		return buildResponseEntity(new APIError(HttpStatus.INTERNAL_SERVER_ERROR, error, ex));
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<Object> handleResourceNotFound(ResourceNotFoundException ex, HttpServletResponse response) throws IOException {
		System.out.println("handleResourceNotFound called");
		String error = "Resource not found";
		return buildResponseEntity(new APIError(HttpStatus.NOT_FOUND, error, ex));
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<Object> hanldeUserNotFoundException(UserNotFoundException ex, HttpServletResponse response) throws IOException {
		System.out.println("UserNotFoundException called");
		String error = "User not found";
		return buildResponseEntity(new APIError(HttpStatus.NOT_FOUND, error, ex));
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<Object> hanldeBadCredentialsException(BadCredentialsException ex, HttpServletResponse response) throws IOException {
		System.out.println("BadCredentialsException called");
		String error = "Invalid username/password entered";
		return buildResponseEntity(new APIError(HttpStatus.BAD_REQUEST, error, ex));
	}

	@ExceptionHandler(ExpiredJwtException.class)
	public ResponseEntity<Object> hanldeExpiredJwtException(ExpiredJwtException ex, HttpServletResponse response) throws IOException {
		System.out.println("ExpiredJwtException called");
		String error = "Session expired for user";
		return buildResponseEntity(new APIError(HttpStatus.UNAUTHORIZED, error, ex));
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleException(Exception ex, HttpServletResponse response) throws IOException {
		System.out.println("handleException called");
		String error = "Exception occurred";
		return buildResponseEntity(new APIError(HttpStatus.INTERNAL_SERVER_ERROR, error, ex));
	}

	private ResponseEntity<Object> buildResponseEntity(APIError apiError) {
		return new ResponseEntity<>(apiError, apiError.getStatus());
	}
}

