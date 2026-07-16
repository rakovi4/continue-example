package com.example.acceptance.statements;

import com.example.acceptance.clients.application.ApplicationClient;
import com.example.acceptance.clients.application.dto.ErrorResponse;
import com.example.acceptance.clients.application.dto.task.CreateTaskRequest;
import com.example.acceptance.clients.application.dto.task.TaskResponse;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.acceptance.statements.AssertionHelpers.assertTimestampRecent;
import static org.assertj.core.api.Assertions.assertThat;

@Service
@RequiredArgsConstructor
public class TaskStatements {

    private final ApplicationClient applicationClient;

    private Response lastResponse;

    public String givenTaskExists(String title) {
        Response response = applicationClient.createTask(new CreateTaskRequest(title));
        return response.as(TaskResponse.class).getId();
    }

    public void whenUserCreatesTaskWithLongTitle() {
        lastResponse = applicationClient.createTask(new CreateTaskRequest("a".repeat(101)));
    }

    public void whenUserCreatesTaskWithLongDescription() {
        lastResponse = applicationClient.createTask(new CreateTaskRequest("Valid title", "a".repeat(5001)));
    }

    public void whenUserCreatesTaskWithEmptyTitle() {
        lastResponse = applicationClient.createTask(new CreateTaskRequest(""));
    }

    public void whenUserCreatesTaskWithTitle(String title) {
        lastResponse = applicationClient.createTask(new CreateTaskRequest(title));
    }

    public void whenUserCreatesTaskWithTitleAndDescription(String title, String description) {
        lastResponse = applicationClient.createTask(new CreateTaskRequest(title, description));
    }

    public void assertTaskCreatedWithTitleOnly(String expectedTitle) {
        assertTaskCreated(expectedTitle, "");
    }

    public void assertTaskCreatedWithTitleAndDescription(String expectedTitle, String expectedDescription) {
        assertTaskCreated(expectedTitle, expectedDescription);
    }

    private void assertTaskCreated(String expectedTitle, String expectedDescription) {
        assertHttpStatus(201);
        TaskResponse task = lastResponse.as(TaskResponse.class);
        assertThat(task.getId()).as("task id").isNotNull();
        assertThat(task.getTitle()).as("task title").isEqualTo(expectedTitle);
        assertThat(task.getDescription()).as("task description").isEqualTo(expectedDescription);
        assertThat(task.getPosition()).as("task position").isEqualTo(0);
        assertTimestampRecent(task.getCreatedAt(), "task creation timestamp");
    }

    public String assertTaskCreatedSuccessfully() {
        assertHttpStatus(201);
        return lastResponse.as(TaskResponse.class).getId();
    }

    public void assertDuplicateTitleError() {
        assertErrorResponse("DUPLICATE_TITLE", "A task with this title already exists");
    }

    public void assertValidationError(String expectedMessage) {
        assertErrorResponse("VALIDATION_ERROR", expectedMessage);
    }

    private void assertErrorResponse(String expectedError, String expectedMessage) {
        assertHttpStatus(400);
        ErrorResponse error = lastResponse.as(ErrorResponse.class);

        assertThat(error.getError()).as("error type").isEqualTo(expectedError);
        assertThat(error.getMessage()).as("error message").isEqualTo(expectedMessage);
        assertTimestampRecent(error.getTimestamp(), "error timestamp");
    }

    private void assertHttpStatus(int expectedStatus) {
        assertThat(lastResponse.statusCode()).as("HTTP status").isEqualTo(expectedStatus);
    }

}
