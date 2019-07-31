package com.security.session.exception;

import org.springframework.http.HttpStatus;

public class InvalidTokenException extends CustomException {

	private static final long serialVersionUID = 1L;

	public InvalidTokenException(String message, HttpStatus httpStatus) {
		super(message, httpStatus);
	}

}
