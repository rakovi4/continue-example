package com.example.acceptance.tests.backend.createTask;

import com.example.acceptance.statements.BoardStatements;
import com.example.acceptance.statements.TaskStatements;
import com.example.acceptance.tests.backend.AbstractBackendTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("4.1 Get board with tasks in To Do")
public class GetBoardWithTasksTest extends AbstractBackendTest {

    @Autowired
    private TaskStatements taskStatements;

    @Autowired
    private BoardStatements boardStatements;

    @Test
    @DisplayName("""
            Given a task "Set up CI/CD" exists in To Do
            When the user requests the board
            Then the To Do column contains task "Set up CI/CD"
            And In Progress and Done columns have no tasks""")
    void should_return_board_with_task_in_to_do() {
        String taskId = taskStatements.givenTaskExists("Set up CI/CD");
        boardStatements.whenUserRequestsBoard();
        boardStatements.assertToDoContainsTask(taskId, "Set up CI/CD");
        boardStatements.assertInProgressAndDoneEmpty();
    }
}
