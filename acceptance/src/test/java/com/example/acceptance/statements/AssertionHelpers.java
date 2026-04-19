package com.example.acceptance.statements;

import com.example.acceptance.clients.application.dto.ErrorResponse;
import io.restassured.response.Response;

import java.time.Instant;

import static java.time.temporal.ChronoUnit.MINUTES;
import static org.assertj.core.api.Assertions.assertThat;

public final class AssertionHelpers {

    private AssertionHelpers() {
    }

    public static void assertTimestampRecent(String timestamp, String description) {
        assertThat(Instant.parse(timestamp).truncatedTo(MINUTES))
                .as(description)
                .isEqualTo(Instant.now().truncatedTo(MINUTES));
    }

    public static void assertErrorResponse(Response response, int expectedStatus,
                                            String expectedError, String expectedMessage) {
        assertThat(response.statusCode()).as("HTTP status").isEqualTo(expectedStatus);
        ErrorResponse error = response.as(ErrorResponse.class);
        assertThat(error.getError()).as("error type").isEqualTo(expectedError);
        assertThat(error.getMessage()).as("error message").isEqualTo(expectedMessage);
        assertTimestampRecent(error.getTimestamp(), "error timestamp");
    }
}
