package com.example.acceptance.tests.backend.createTask;

import com.example.acceptance.statements.TaskStatements;
import com.example.acceptance.tests.backend.AbstractBackendTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("2.1 Reject empty title")
public class RejectEmptyTitleTest extends AbstractBackendTest {

    @Autowired
    private TaskStatements taskStatements;

    @Test
    @DisplayName("""
            When the user creates a task with an empty title
            Then the task is rejected with a validation error
            And the error message indicates the title is required""")
    void should_reject_task_with_empty_title() {
        taskStatements.whenUserCreatesTaskWithEmptyTitle();
        taskStatements.assertValidationError("Title is required");
    }
}
