package com.example.rest.dto;

import lombok.Value;

import java.time.Instant;

@Value
public class ErrorResponseDto {

    String error;
    String message;
    Instant timestamp;

    public static ErrorResponseDto of(String error, String message) {
        return new ErrorResponseDto(error, message, Instant.now());
    }
}
