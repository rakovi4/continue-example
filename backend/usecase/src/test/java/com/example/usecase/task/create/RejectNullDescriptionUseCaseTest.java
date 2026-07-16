package com.example.usecase.task.create;

import com.example.usecase.ApplicationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("3.1 Description rejects null value (coverage)")
class RejectNullDescriptionUseCaseTest extends ApplicationTest {

    @Test
    @DisplayName("""
            When the user creates a task with a null description
            Then the task is rejected with a validation error
            And the error message indicates the description must not be null""")
    void should_reject_null_description() {
        taskStatements.createTaskWithNullDescription();
        taskStatements.assertValidationError("Description must not be null");
    }
}
