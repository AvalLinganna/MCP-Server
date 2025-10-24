package com.zurich.poc.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {
        ApiResponse<Void> response = ApiResponse.error(HttpStatus.NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationExceptions(
            MethodArgumentNotValidException ex, WebRequest request) {
        List<ApiResponse.ValidationError> errors = ApiResponse.ValidationError.fromFieldErrors(ex.getBindingResult());
        ApiResponse<Void> response = ApiResponse.validationError(errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ApiResponse<Void>> handleBindExceptions(
            BindException ex, WebRequest request) {
        List<ApiResponse.ValidationError> errors = ApiResponse.ValidationError.fromFieldErrors(ex.getBindingResult());
        ApiResponse<Void> response = ApiResponse.validationError(errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolationException(
            ConstraintViolationException ex, WebRequest request) {
        List<ApiResponse.ValidationError> errors = new ArrayList<>();
        ex.getConstraintViolations().forEach(violation -> {
            String fieldName = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            errors.add(new ApiResponse.ValidationError(fieldName, message));
        });
        ApiResponse<Void> response = ApiResponse.validationError(errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ApiResponse<Void>> handleHttpClientErrorException(
            HttpClientErrorException ex, WebRequest request) {
        ApiResponse<Void> response = ApiResponse.error(
            HttpStatus.valueOf(ex.getStatusCode().value()), 
            ex.getMessage());
        return new ResponseEntity<>(response, ex.getStatusCode());
    }
    
    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceAccessException(
            ResourceAccessException ex, WebRequest request) {
        ApiResponse<Void> response = ApiResponse.error(
                HttpStatus.SERVICE_UNAVAILABLE, 
                "External service is unavailable: " + ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGlobalException(
            Exception ex, WebRequest request) {
        ApiResponse<Void> response = ApiResponse.error(
                HttpStatus.INTERNAL_SERVER_ERROR, 
                "An unexpected error occurred: " + ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
