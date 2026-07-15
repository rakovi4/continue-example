package com.example.usecase;

import com.example.domain.board.Board;
import com.example.domain.board.Column;
import com.example.domain.board.ColumnType;
import com.example.usecase.board.GetBoardUseCase;
import com.example.usecase.fake.board.FakeBoardStorage;
import com.example.usecase.statements.BoardStatements;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;

public class ApplicationTest {

    // ==================== FAKES ====================
    protected FakeBoardStorage fakeBoardStorage;

    // ==================== USECASES ====================
    protected GetBoardUseCase getBoardUseCase;

    // ==================== TEST STATEMENTS ====================
    protected BoardStatements boardStatements;

    @BeforeEach
    void setUp() {
        initFakes();
        initUseCases();
        initStatements();
    }

    private void initFakes() {
        fakeBoardStorage = new FakeBoardStorage();
        fakeBoardStorage.setBoard(new Board(List.of(
                Column.empty(ColumnType.TO_DO),
                Column.empty(ColumnType.IN_PROGRESS),
                Column.empty(ColumnType.DONE)
        )));
    }

    private void initUseCases() {
        getBoardUseCase = new GetBoardUseCase(fakeBoardStorage);
    }

    private void initStatements() {
        boardStatements = new BoardStatements(getBoardUseCase);
    }
}
