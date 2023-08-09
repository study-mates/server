package org.studymate.global;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.studymate.global.exception.BadRequestException;
import org.studymate.global.exception.ForbiddenException;
import org.studymate.global.exception.InternalServerErrorException;
import org.studymate.global.exception.NotFoundException;
import org.studymate.global.exception.UnauthorizedException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@Data
	@AllArgsConstructor
	class ErrorResponse {
		LocalDateTime timestamp;
		int status;
		String error;
		String message;
		String path;
 
	}

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<?> badRequestExceptionHandler(BadRequestException e, HttpServletRequest request) {
		ErrorResponse response = new ErrorResponse(LocalDateTime.now(), 400, "Bad Requset", e.getMessage(),
				request.getRequestURI());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
	
	
	@ExceptionHandler(ForbiddenException.class)
	public ResponseEntity<?> forbiddenExceptionHandler(ForbiddenException e, HttpServletRequest request) {
		ErrorResponse response = new ErrorResponse(LocalDateTime.now(), 400, "Forbidden", e.getMessage(),
				request.getRequestURI());
		return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<?> notFoundExceptionHandler(NotFoundException e, HttpServletRequest request) {
		ErrorResponse response = new ErrorResponse(LocalDateTime.now(), 404, "Not found", e.getMessage(),
				request.getRequestURI());
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(InternalServerErrorException.class)
	public ResponseEntity<?> internalSeverErrorHandler(InternalServerErrorException e, HttpServletRequest request) {
		ErrorResponse response = new ErrorResponse(LocalDateTime.now(), 500, "Internal Server Error", e.getMessage(),
				request.getRequestURI());
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<?> unauthorizedExceptionHandler(UnauthorizedException e, HttpServletRequest request) {
		ErrorResponse response = new ErrorResponse(LocalDateTime.now(), 401, "Unauthroized", e.getMessage(),
				request.getRequestURI());
		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
	}
}
