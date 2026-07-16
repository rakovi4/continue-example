package com.example.usecase.board.get;

import com.example.usecase.ApplicationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("4.1 Get board with tasks in To Do")
class GetBoardWithTasksInToDoUseCaseTest extends ApplicationTest {

    @Test
    @DisplayName("""
            Given a task "Set up CI/CD" exists in To Do
            When the user requests the board
            Then the To Do column contains task "Set up CI/CD"
            And In Progress and Done columns have no tasks""")
    void should_return_board_with_task_in_to_do_column() {
        taskStatements.createTaskWithTitleOnly("Set up CI/CD");
        boardStatements.getBoard();
        boardStatements.assertBoardHasOnlyTaskInToDo("Set up CI/CD");
    }
}
