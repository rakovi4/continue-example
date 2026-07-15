package com.example.domain.task;

import com.example.domain.exception.ValidationException;
import lombok.Value;

@Value
public class Description {

    String value;

    public Description(String value) {
        if (value == null) {
            throw new ValidationException("Description must not be null");
        }
        if (value.length() > 5000) {
            throw new ValidationException("Description must not exceed 5000 characters");
        }
        this.value = value;
    }
}
