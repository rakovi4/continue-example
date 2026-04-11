package com.example.acceptance.tests.backend.createTask;

import com.example.acceptance.statements.TaskStatements;
import com.example.acceptance.tests.backend.AbstractBackendTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("3.2 Create task with title and description")
public class CreateTaskWithDescriptionTest extends AbstractBackendTest {

    @Autowired
    private TaskStatements taskStatements;

    @Test
    @DisplayName("""
            When the user creates a task with title "Set up CI/CD" and description "Configure GitHub Actions"
            Then the task is created successfully
            And the response contains the task with title "Set up CI/CD" and description "Configure GitHub Actions\"""")
    void should_create_task_with_title_and_description() {
        taskStatements.whenUserCreatesTaskWithTitleAndDescription("Set up CI/CD", "Configure GitHub Actions");
        taskStatements.assertTaskCreatedWithTitleAndDescription("Set up CI/CD", "Configure GitHub Actions");
    }
}
