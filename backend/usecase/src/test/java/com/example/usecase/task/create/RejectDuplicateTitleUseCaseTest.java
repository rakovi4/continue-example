package com.example.usecase.task.create;

import com.example.usecase.ApplicationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("5.1 Reject duplicate task title")
class RejectDuplicateTitleUseCaseTest extends ApplicationTest {

    @Test
    @DisplayName("""
            Given a task "Set up CI/CD" exists in To Do
            When the user creates a task with title "Set up CI/CD"
            Then the task is rejected with a duplicate title error""")
    void should_reject_duplicate_task_title() {
        taskStatements.givenTaskExists("Set up CI/CD");
        taskStatements.createDuplicateTask("Set up CI/CD");
        taskStatements.assertDuplicateTitleError();
    }
}
