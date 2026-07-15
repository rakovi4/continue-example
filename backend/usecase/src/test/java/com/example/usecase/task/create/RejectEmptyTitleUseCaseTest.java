package com.example.usecase.task.create;

import com.example.usecase.ApplicationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("2.1 Reject empty title")
class RejectEmptyTitleUseCaseTest extends ApplicationTest {

    @Test
    @DisplayName("""
            When the user creates a task with an empty title
            Then the task is rejected with a validation error
            And the error message indicates the title is required""")
    void should_reject_empty_title() {
        taskStatements.createTaskWithEmptyTitle();
        taskStatements.assertValidationError("Title is required");
    }
}
