package com.example.acceptance.tests.backend.createTask;

import com.example.acceptance.statements.TaskStatements;
import com.example.acceptance.tests.backend.AbstractBackendTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("2.2 Reject title exceeding 100 characters")
public class RejectLongTitleTest extends AbstractBackendTest {

    @Autowired
    private TaskStatements taskStatements;

    @Test
    @DisplayName("""
            When the user creates a task with a title of 101 characters
            Then the task is rejected with a validation error
            And the error message indicates the title is too long""")
    void should_reject_task_with_title_exceeding_100_characters() {
        taskStatements.whenUserCreatesTaskWithLongTitle();
        taskStatements.assertValidationError("Title must not exceed 100 characters");
    }
}
