package com.example.rest.controller.task;

import com.example.rest.dto.task.CreateTaskRequestDto;
import com.example.rest.dto.task.TaskResponse;
import com.example.usecase.task.CreateTaskUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class TaskController {

    private final CreateTaskUseCase createTaskUseCase;

    @PostMapping("/tasks")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponse createTask(@RequestBody CreateTaskRequestDto request) {
        return TaskResponse.from(createTaskUseCase.createTask(request.toUsecaseRequest()));
    }
}
