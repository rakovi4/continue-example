package com.example.domain.task;

import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
public class Task {

    UUID id;
    Title title;
    Description description;
    int position;
    Instant createdAt;

    public static Task create(Title title, Description description, int position) {
        return new Task(UUID.randomUUID(), title, description, position, Instant.now());
    }
}
