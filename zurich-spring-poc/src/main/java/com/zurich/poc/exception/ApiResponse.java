package com.zurich.poc.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    
    private HttpStatus status;
    private int statusCode;
    private String message;
    private T data;
    private LocalDateTime timestamp;
    private List<ValidationError> errors;
    
    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    public static <T> ApiResponse<T> error(HttpStatus status, String message) {
        return ApiResponse.<T>builder()
                .status(status)
                .statusCode(status.value())
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    public static <T> ApiResponse<T> validationError(List<ValidationError> errors) {
        return ApiResponse.<T>builder()
                .status(HttpStatus.BAD_REQUEST)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message("Validation failed")
                .errors(errors)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ValidationError {
        private String field;
        private String message;
        
        public static List<ValidationError> fromFieldErrors(org.springframework.validation.Errors errors) {
            List<ValidationError> validationErrors = new ArrayList<>();
            errors.getFieldErrors().forEach(error -> 
                validationErrors.add(new ValidationError(error.getField(), error.getDefaultMessage()))
            );
            return validationErrors;
        }
    }
}
