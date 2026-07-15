package com.example.usecase.board.get;

import com.example.usecase.ApplicationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("1.1 Get empty board")
class GetEmptyBoardUseCaseTest extends ApplicationTest {

    @Test
    @DisplayName("""
            Given a board with three columns
            When the user requests the board
            Then the response contains the board with columns To Do, In Progress, Done
            And each column has no tasks""")
    void should_return_board_with_three_empty_columns() {
        boardStatements.getBoard();
        boardStatements.assertBoardHasThreeEmptyColumns();
    }
}
