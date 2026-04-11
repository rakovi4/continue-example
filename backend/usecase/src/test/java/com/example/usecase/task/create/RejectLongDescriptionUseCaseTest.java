package com.example.usecase.task.create;

import com.example.usecase.ApplicationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("2.3 Reject description exceeding 5000 characters")
class RejectLongDescriptionUseCaseTest extends ApplicationTest {

    @Test
    @DisplayName("""
            When the user creates a task with a valid title and a description of 5001 characters
            Then the task is rejected with a validation error
            And the error message indicates the description is too long""")
    void should_reject_description_exceeding_5000_characters() {
        taskStatements.createTaskWithLongDescription(5001);
        taskStatements.assertValidationError("Description must not exceed 5000 characters");
    }
}
