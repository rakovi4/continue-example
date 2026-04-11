package com.example.usecase.fake.board;

import com.example.domain.board.Board;
import com.example.usecase.board.BoardStorage;

public class FakeBoardStorage implements BoardStorage {

    private Board board;

    public void setBoard(Board board) {
        this.board = board;
    }

    @Override
    public Board getBoard() {
        return board;
    }

    @Override
    public void save(Board board) {
        this.board = board;
    }
}
