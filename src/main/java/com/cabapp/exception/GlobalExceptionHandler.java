package com.cabapp.exception;

import com.cabapp.dto.ApiErrorDto;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Handle Resource Not Found (404)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorDto> handleResourceNotFound(ResourceNotFoundException ex) {
        ApiErrorDto apiErrorDto = new ApiErrorDto(HttpStatus.NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(apiErrorDto, HttpStatus.NOT_FOUND);
    }

    // 2. Handle User Already Exists (409)
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiErrorDto> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        ApiErrorDto apiErrorDto = new ApiErrorDto(HttpStatus.CONFLICT, ex.getMessage());
        return new ResponseEntity<>(apiErrorDto, HttpStatus.CONFLICT);
    }

    // 3. Handle Bad Logic / Illegal State (400)
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiErrorDto> handleIllegalState(IllegalStateException ex) {
        ApiErrorDto apiErrorDto = new ApiErrorDto(HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(apiErrorDto, HttpStatus.BAD_REQUEST);
    }

    // 4. Handle Validation Errors (400) - e.g. "Email invalid"
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorDto> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        ApiErrorDto apiErrorDto = new ApiErrorDto(HttpStatus.BAD_REQUEST, "Input validation failed", errors);
        return new ResponseEntity<>(apiErrorDto, HttpStatus.BAD_REQUEST);
    }

    // 5. Handle Security / Access Denied (403)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorDto> handleAccessDenied(AccessDeniedException ex) {
        ApiErrorDto apiErrorDto = new ApiErrorDto(HttpStatus.FORBIDDEN, "You do not have permission to access this resource");
        return new ResponseEntity<>(apiErrorDto, HttpStatus.FORBIDDEN);
    }

    // 6. Handle All Other Exceptions (500) - Fallback
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorDto> handleGlobalException(Exception ex) {
        // Log the full stack trace internally here if using a logger
        ApiErrorDto apiErrorDto = new ApiErrorDto(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        return new ResponseEntity<>(apiErrorDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}