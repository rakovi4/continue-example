package com.example.h2.access.board;

import com.example.domain.board.Board;
import com.example.domain.board.Column;
import com.example.domain.board.ColumnType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("1.1 Get empty board")
class H2BoardStorageGetBoardTest extends AbstractBoardStorageTest {

    @Test
    @DisplayName("""
            Given a board with three columns
            When the user requests the board
            Then the response contains the board with columns To Do, In Progress, Done
            And each column has no tasks""")
    void should_return_board_with_three_empty_columns() {
        Board board = h2BoardStorage.getBoard();

        assertThat(board.getColumns())
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(
                        Column.empty(ColumnType.TO_DO),
                        Column.empty(ColumnType.IN_PROGRESS),
                        Column.empty(ColumnType.DONE)
                );
    }
}
