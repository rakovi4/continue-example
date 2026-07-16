package com.example.acceptance.tests.backend.createTask;

import com.example.acceptance.statements.TaskStatements;
import com.example.acceptance.tests.backend.AbstractBackendTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("3.1 Create task with title only")
public class CreateTaskWithTitleTest extends AbstractBackendTest {

    @Autowired
    private TaskStatements taskStatements;

    @Test
    @DisplayName("""
            When the user creates a task with title "Set up CI/CD"
            Then the task is created successfully
            And the response contains the task with title "Set up CI/CD" and no description
            And the task has a position and creation timestamp""")
    void should_create_task_with_title_only() {
        taskStatements.whenUserCreatesTaskWithTitle("Set up CI/CD");
        taskStatements.assertTaskCreatedWithTitleOnly("Set up CI/CD");
    }
}
