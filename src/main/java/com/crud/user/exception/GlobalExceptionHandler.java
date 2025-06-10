package com.crud.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> userAlreadyExistsHandling(UserAlreadyExistsException exception) {
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    // Handles validation errors and returns HTTP 400 Bad Request
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> customValidationErrorHandling(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();

        exception.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    // handling global exception
   @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> globalExceptionHandling(Exception exception) {
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "Error occurred");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

