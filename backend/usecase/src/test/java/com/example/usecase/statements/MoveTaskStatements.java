package com.example.usecase.statements;

import com.example.domain.exception.TaskNotFoundException;
import com.example.usecase.task.MoveTaskRequest;
import com.example.usecase.task.MoveTaskUseCase;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@RequiredArgsConstructor
public class MoveTaskStatements {

    private final MoveTaskUseCase moveTaskUseCase;

    private Throwable caughtException;

    public void moveNonExistentTask() {
        caughtException = catchThrowable(() ->
                moveTaskUseCase.moveTask(new MoveTaskRequest(UUID.randomUUID(), 1L, 0)));
    }

    public void assertTaskNotFoundError() {
        assertThat(caughtException)
                .as("moving non-existent task should throw TaskNotFoundException")
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessage("Task not found");
    }
}
