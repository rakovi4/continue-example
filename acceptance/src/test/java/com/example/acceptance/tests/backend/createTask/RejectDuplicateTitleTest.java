package com.example.acceptance.tests.backend.createTask;

import com.example.acceptance.statements.TaskStatements;
import com.example.acceptance.tests.backend.AbstractBackendTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("5.1 Reject duplicate task title")
public class RejectDuplicateTitleTest extends AbstractBackendTest {

    @Autowired
    private TaskStatements taskStatements;

    @Test
    @DisplayName("""
            Given a task "Set up CI/CD" exists in To Do
            When the user creates a task with title "Set up CI/CD"
            Then the task is rejected with a duplicate title error""")
    void should_reject_duplicate_task_title() {
        taskStatements.givenTaskExists("Set up CI/CD");
        taskStatements.whenUserCreatesTaskWithTitle("Set up CI/CD");
        taskStatements.assertDuplicateTitleError();
    }
}
