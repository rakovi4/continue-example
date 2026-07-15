package com.example.acceptance.statements;

import com.example.acceptance.clients.application.ApplicationClient;
import com.example.acceptance.clients.application.dto.ErrorResponse;
import com.example.acceptance.clients.application.dto.task.CreateTaskRequest;
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

    public void whenUserCreatesTaskWithLongTitle() {
        lastResponse = applicationClient.createTask(new CreateTaskRequest("a".repeat(101)));
    }

    public void whenUserCreatesTaskWithLongDescription() {
        lastResponse = applicationClient.createTask(new CreateTaskRequest("Valid title", "a".repeat(5001)));
    }

    public void whenUserCreatesTaskWithEmptyTitle() {
        lastResponse = applicationClient.createTask(new CreateTaskRequest(""));
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
