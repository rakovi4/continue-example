package com.example.usecase.task;

import lombok.Value;

import java.util.UUID;

@Value
public class MoveTaskRequest {

    UUID taskId;
    long columnId;
    int position;
}
