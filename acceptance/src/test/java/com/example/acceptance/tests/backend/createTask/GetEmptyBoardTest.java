package com.example.acceptance.tests.backend.createTask;

import com.example.acceptance.statements.BoardStatements;
import com.example.acceptance.tests.backend.AbstractBackendTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("1.1 Get empty board")
public class GetEmptyBoardTest extends AbstractBackendTest {

    @Autowired
    private BoardStatements boardStatements;

    @Test
    @DisplayName("""
            Given a board with three columns
            When the user requests the board
            Then the response contains the board with columns To Do, In Progress, Done
            And each column has no tasks""")
    void should_return_board_with_three_empty_columns() {
        boardStatements.whenUserRequestsBoard();
        boardStatements.assertBoardHasThreeEmptyColumns();
    }
}
