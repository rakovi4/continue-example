package com.example.rest.controller.task;

import com.example.rest.dto.task.CreateTaskRequestDto;
import com.example.rest.dto.task.MoveTaskRequestDto;
import com.example.rest.dto.task.TaskResponseDto;
import com.example.usecase.task.CreateTaskUseCase;
import com.example.usecase.task.MoveTaskUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class TaskController {

    private final CreateTaskUseCase createTaskUseCase;
    private final MoveTaskUseCase moveTaskUseCase;

    @PostMapping("/tasks")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponseDto createTask(@RequestBody CreateTaskRequestDto request) {
        var task = createTaskUseCase.createTask(request.toUsecaseRequest());
        return TaskResponseDto.from(task);
    }

    @PatchMapping("/tasks/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void moveTask(@PathVariable UUID id, @RequestBody MoveTaskRequestDto request) {
        moveTaskUseCase.moveTask(request.toUsecaseRequest(id));
    }
}
