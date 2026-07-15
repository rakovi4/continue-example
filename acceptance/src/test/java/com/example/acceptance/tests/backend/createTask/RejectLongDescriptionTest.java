package com.example.acceptance.tests.backend.createTask;

import com.example.acceptance.statements.TaskStatements;
import com.example.acceptance.tests.backend.AbstractBackendTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("2.3 Reject description exceeding 5000 characters")
public class RejectLongDescriptionTest extends AbstractBackendTest {

    @Autowired
    private TaskStatements taskStatements;

    @Test
    @DisplayName("""
            When the user creates a task with a valid title and a description of 5001 characters
            Then the task is rejected with a validation error
            And the error message indicates the description is too long""")
    void should_reject_task_with_description_exceeding_5000_characters() {
        taskStatements.whenUserCreatesTaskWithLongDescription();
        taskStatements.assertValidationError("Description must not exceed 5000 characters");
    }
}
