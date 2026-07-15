package com.example.usecase.statements;

import com.example.domain.board.Board;
import com.example.domain.board.Column;
import com.example.domain.board.ColumnType;
import com.example.usecase.board.GetBoardUseCase;
import lombok.RequiredArgsConstructor;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
public class BoardStatements {

    private final GetBoardUseCase getBoardUseCase;

    private Board boardResult;

    public void getBoard() {
        boardResult = getBoardUseCase.getBoard();
    }

    public void assertBoardHasThreeEmptyColumns() {
        assertThat(boardResult.getColumns())
                .as("board columns")
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(
                        Column.empty(ColumnType.TO_DO),
                        Column.empty(ColumnType.IN_PROGRESS),
                        Column.empty(ColumnType.DONE)
                );
    }
}
