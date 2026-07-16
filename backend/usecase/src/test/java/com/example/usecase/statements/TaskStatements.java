package com.example.usecase.statements;

import com.example.domain.exception.ValidationException;
import com.example.domain.task.Task;
import com.example.domain.task.Title;
import com.example.usecase.scope.TestData;
import com.example.usecase.task.CreateTaskRequest;
import com.example.usecase.task.CreateTaskUseCase;
import lombok.RequiredArgsConstructor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@RequiredArgsConstructor
public class TaskStatements {

    private static final String VALID_TITLE = "Valid title";
    private static final String NEW_TASK_TITLE = "Set up CI/CD";
    private static final int FIRST_POSITION = 1;

    private final CreateTaskUseCase createTaskUseCase;

    private Throwable caughtException;
    private Task createdTask;

    public void createTaskWithTitleOnly() {
        createdTask = createTaskUseCase.createTask(new CreateTaskRequest(NEW_TASK_TITLE));
    }

    public void assertTaskCreatedWithTitleOnly() {
        assertThat(createdTask.getId()).as("created task id").isNotNull();
        assertThat(createdTask)
                .as("created task")
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(new Task(null, new Title(NEW_TASK_TITLE), null, FIRST_POSITION, TestData.CREATED_AT));
    }

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
