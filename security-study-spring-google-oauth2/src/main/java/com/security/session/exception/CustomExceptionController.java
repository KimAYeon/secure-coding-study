package com.security.session.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionController {
	@ExceptionHandler(value=CustomException.class)
	public ResponseEntity<Object> showMessage(CustomException customException) {
		return new ResponseEntity<Object>(customException.getMessage(), customException.getHttpStatus());
	}
}
