package com.example.rest.controller.task;

import com.example.rest.dto.task.CreateTaskRequestDto;
import com.example.rest.dto.task.TaskResponseDto;
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
    public TaskResponseDto createTask(@RequestBody CreateTaskRequestDto request) {
        var task = createTaskUseCase.createTask(request.toUsecaseRequest());
        return TaskResponseDto.from(task);
    }
}
