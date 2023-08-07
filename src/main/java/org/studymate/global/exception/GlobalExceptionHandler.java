package org.studymate.global.exception;


import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.studymate.global.exception.type.BadRequestException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	
	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<?> badRequestExceptionHandler(BadRequestException e) {
		System.out.println("????" + e);
		return new ResponseEntity<>(Map.of("message", e.getMessage() ), HttpStatus.BAD_REQUEST);
	}
}
