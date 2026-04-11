package com.example.usecase.statements;

import com.example.domain.exception.DuplicateTitleException;
import com.example.domain.exception.ValidationException;
import com.example.domain.task.Task;
import com.example.usecase.task.CreateTaskRequest;
import com.example.usecase.task.CreateTaskUseCase;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@RequiredArgsConstructor
public class TaskStatements {

    private static final String VALID_TITLE = "Valid title";

    private final CreateTaskUseCase createTaskUseCase;

    private Throwable caughtException;
    private Task createdTask;

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

    public void createTaskWithNullDescription() {
        createTaskExpectingError(new CreateTaskRequest(VALID_TITLE, null));
    }

    private void createTaskExpectingError(CreateTaskRequest request) {
        caughtException = catchThrowable(() -> createTaskUseCase.createTask(request));
    }

    public void givenTaskExists(String title) {
        createTaskUseCase.createTask(new CreateTaskRequest(title));
    }

    public void createTaskWithTitleOnly(String title) {
        createdTask = createTaskUseCase.createTask(new CreateTaskRequest(title));
    }

    public void createTaskWithTitleAndDescription(String title, String description) {
        createdTask = createTaskUseCase.createTask(new CreateTaskRequest(title, description));
    }

    public void createDuplicateTask(String title) {
        createTaskExpectingError(new CreateTaskRequest(title));
    }

    public void assertDuplicateTitleError() {
        assertThat(caughtException)
                .isInstanceOf(DuplicateTitleException.class)
                .hasMessage("A task with this title already exists");
    }

    public void assertTaskCreatedWithTitleOnly(String expectedTitle) {
        assertTaskCreated(expectedTitle, "", 0);
    }

    public void assertSecondTaskCreated(String expectedTitle) {
        assertTaskCreated(expectedTitle, "", 1);
    }

    public void assertTaskCreatedWithTitleAndDescription(String expectedTitle, String expectedDescription) {
        assertTaskCreated(expectedTitle, expectedDescription, 0);
    }

    private void assertTaskCreated(String expectedTitle, String expectedDescription, int expectedPosition) {
        assertValidUuid(createdTask.getId());
        assertThat(createdTask.getTitle().getValue()).as("task title").isEqualTo(expectedTitle);
        assertThat(createdTask.getDescription().getValue()).as("task description").isEqualTo(expectedDescription);
        assertThat(createdTask.getPosition()).as("task position").isEqualTo(expectedPosition);
        assertRecentTimestamp(createdTask.getCreatedAt());
    }

    public void assertValidationError(String expectedMessage) {
        assertThat(caughtException)
                .isInstanceOf(ValidationException.class)
                .hasMessage(expectedMessage);
    }

    private void assertValidUuid(UUID id) {
        assertThat(id).as("task id").isNotNull();
        assertThat(id.toString()).as("task id format")
                .matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}");
    }

    private void assertRecentTimestamp(Instant timestamp) {
        assertThat(timestamp).as("task creation timestamp").isBetween(
                Instant.now().minusSeconds(30), Instant.now().plusSeconds(30));
    }
}
