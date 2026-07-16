package com.example.usecase.board.add;

import com.example.usecase.ApplicationTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Disabled("TDD Red Phase - Board.addTask throws UnsupportedOperationException instead of IllegalStateException")
@DisplayName("3.1 Create task with title only - board loaded without a To Do column")
class AddTaskWithoutToDoColumnTest extends ApplicationTest {

    @Test
    @DisplayName("""
            Given a board loaded without a To Do column
            When a task is added to the board
            Then the board fails loudly with a missing To Do column error""")
    void should_fail_loudly_when_board_has_no_to_do_column() {
        boardStatements.addTaskToBoardWithoutToDoColumn();
        boardStatements.assertMissingToDoColumnError();
    }
}
