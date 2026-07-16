package com.example.usecase.task.create;

import com.example.usecase.ApplicationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("3.1 Create task with title only")
class CreateTaskWithTitleOnlyUseCaseTest extends ApplicationTest {

    @Test
    @DisplayName("""
            When the user creates a task with title "Set up CI/CD"
            Then the task is created successfully
            And the response contains the task with title "Set up CI/CD" and no description
            And the task has a position and creation timestamp""")
    void should_create_task_with_title_only() {
        taskStatements.createTaskWithTitleOnly("Set up CI/CD");
        taskStatements.assertTaskCreatedWithTitleOnly("Set up CI/CD");
    }
}
