package com.wistrum.taskfetch.api.exception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.wistrum.taskfetch.api.controller.TaskController;

@RestControllerAdvice
public class GlobalExceptionHandler {
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<String> handleEntityNotFoundException (EntityNotFoundException e){
		logger.error("Entity Not Found: {}", e.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entity Not Found " + e.getMessage());
	}
	@ExceptionHandler(IllegalInputException.class)
	public ResponseEntity<String> handleInvalidInputException (IllegalInputException e) {
		logger.error("Illegal Input: {}", e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Input " + e.getMessage());
	}
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<String> handleConstraintViolationException (ConstraintViolationException e) {
		logger.error("Constraint Violation: {}", e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Constraint Violation " + e.getMessage());
	}
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> handleMethodArgumentNotValidException (MethodArgumentNotValidException e) {
		BindingResult bindingResult = e.getBindingResult();
		Map<String, String> errorMap = new HashMap<>();
		List<FieldError> fieldErrors = bindingResult.getFieldErrors();
		StringBuilder errorMessage = new StringBuilder("Validation Failed: ");
		for(FieldError fieldError : fieldErrors) {
			errorMessage.append(fieldError.getField())
						.append(" - ")
						.append(fieldError.getDefaultMessage())
						.append(" .");
		}
		errorMap.put("error", errorMessage.toString().trim());
		logger.error("MethodArgumentNotValidException: {}", errorMessage.toString()  );
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.contentType(MediaType.APPLICATION_JSON)
				.body(errorMap);
	}
}