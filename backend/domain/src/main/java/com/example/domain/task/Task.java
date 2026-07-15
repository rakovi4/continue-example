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
}
