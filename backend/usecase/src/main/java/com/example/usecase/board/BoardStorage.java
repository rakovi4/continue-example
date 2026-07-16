package com.example.usecase.board;

import com.example.domain.board.Board;

public interface BoardStorage {

    Board getBoard();

    void save(Board board);
}
