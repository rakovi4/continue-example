package com.example.domain.task;

import com.example.domain.exception.ValidationException;
import lombok.Value;

@Value
public class Title {

    String value;

    public Title(String value) {
        if (value == null || value.isBlank()) {
            throw new ValidationException("Title is required");
        }
        if (value.length() > 100) {
            throw new ValidationException("Title must not exceed 100 characters");
        }
        this.value = value;
    }
}
