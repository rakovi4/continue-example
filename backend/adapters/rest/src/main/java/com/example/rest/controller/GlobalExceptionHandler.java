package com.example.rest.controller;

import com.example.domain.exception.DuplicateTitleException;
import com.example.domain.exception.ValidationException;
import com.example.rest.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponseDto> handleValidation(ValidationException ex) {
        return badRequest("VALIDATION_ERROR", ex.getMessage());
    }

    @ExceptionHandler(DuplicateTitleException.class)
    public ResponseEntity<ErrorResponseDto> handleDuplicateTitle(DuplicateTitleException ex) {
        return badRequest("DUPLICATE_TITLE", ex.getMessage());
    }

    private ResponseEntity<ErrorResponseDto> badRequest(String error, String message) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponseDto.of(error, message));
    }
}
