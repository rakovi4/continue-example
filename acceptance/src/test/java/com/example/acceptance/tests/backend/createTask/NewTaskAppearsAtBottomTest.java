package com.example.acceptance.tests.backend.createTask;

import com.example.acceptance.statements.BoardStatements;
import com.example.acceptance.statements.TaskStatements;
import com.example.acceptance.tests.backend.AbstractBackendTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("4.2 New task appears at bottom of To Do column")
public class NewTaskAppearsAtBottomTest extends AbstractBackendTest {

    @Autowired
    private TaskStatements taskStatements;

    @Autowired
    private BoardStatements boardStatements;

    @Test
    @DisplayName("""
            Given a task "First task" exists in To Do
            When the user creates a task with title "Second task"
            Then the task is created successfully
            When the user requests the board
            Then the To Do column contains "First task" before "Second task"
            """)
    void should_return_tasks_in_creation_order() {
        String firstTaskId = taskStatements.givenTaskExists("First task");
        taskStatements.whenUserCreatesTaskWithTitle("Second task");
        String secondTaskId = taskStatements.assertTaskCreatedSuccessfully();
        boardStatements.whenUserRequestsBoard();
        boardStatements.assertToDoContainsTasksInOrder(firstTaskId, "First task", secondTaskId, "Second task");
    }
}
