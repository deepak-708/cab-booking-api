package com.cabapp.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ApiErrorDto {
    private LocalDateTime timestamp;
    private Integer status;
    private String error;
    private String message;
    private List<String> subErrors; // For validation errors (e.g. "Email is invalid")

    public ApiErrorDto(HttpStatus status, String message, List<String> subErrors) {
        this.timestamp = LocalDateTime.now();
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.message = message;
        this.subErrors = subErrors;
    }

    public ApiErrorDto(HttpStatus status, String message) {
        this(status, message, null);
    }
}