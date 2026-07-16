package com.example.usecase.statements;

import com.example.domain.board.Board;
import com.example.domain.board.Column;
import com.example.domain.board.ColumnType;
import com.example.domain.task.Title;
import com.example.usecase.board.GetBoardUseCase;
import com.example.usecase.scope.TestData;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@RequiredArgsConstructor
public class BoardStatements {

    private static final String NEW_TASK_TITLE = "Set up CI/CD";

    private final GetBoardUseCase getBoardUseCase;

    private Board boardResult;
    private Throwable caughtException;

    public void getBoard() {
        boardResult = getBoardUseCase.getBoard();
    }

    public void addTaskToBoardWithoutToDoColumn() {
        var board = new Board(List.of(
                Column.empty(ColumnType.IN_PROGRESS),
                Column.empty(ColumnType.DONE)
        ));
        caughtException = catchThrowable(
                () -> board.addTask(new Title(NEW_TASK_TITLE), null, TestData.CREATED_AT));
    }

    public void assertMissingToDoColumnError() {
        assertThat(caughtException)
                .as("missing To Do column failure")
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Board has no To Do column");
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
