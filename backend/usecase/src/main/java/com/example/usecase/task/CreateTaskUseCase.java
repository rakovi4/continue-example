package com.example.usecase.task;

import org.springframework.stereotype.Service;

@Service
public class CreateTaskUseCase {

    public void createTask(CreateTaskRequest request) {
        request.toTitle();
        request.toDescription();
    }
}
