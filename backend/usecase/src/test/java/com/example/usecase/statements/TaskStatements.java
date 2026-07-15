package com.example.usecase.statements;

import com.example.domain.exception.ValidationException;
import com.example.usecase.task.CreateTaskRequest;
import com.example.usecase.task.CreateTaskUseCase;
import lombok.RequiredArgsConstructor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@RequiredArgsConstructor
public class TaskStatements {

    private static final String VALID_TITLE = "Valid title";

    private final CreateTaskUseCase createTaskUseCase;

    private Throwable caughtException;

    public void createTaskWithEmptyTitle() {
        createTaskExpectingError(new CreateTaskRequest(""));
    }

    public void createTaskWithLongTitle(int length) {
        createTaskExpectingError(new CreateTaskRequest("a".repeat(length)));
    }

    public void createTaskWithLongDescription(int length) {
        createTaskExpectingError(new CreateTaskRequest(VALID_TITLE, "a".repeat(length)));
    }

    public void createTaskWithNullTitle() {
        createTaskExpectingError(new CreateTaskRequest(null));
    }

    private void createTaskExpectingError(CreateTaskRequest request) {
        caughtException = catchThrowable(() -> createTaskUseCase.createTask(request));
    }

    public void assertValidationError(String expectedMessage) {
        assertThat(caughtException)
                .isInstanceOf(ValidationException.class)
                .hasMessage(expectedMessage);
    }
}
