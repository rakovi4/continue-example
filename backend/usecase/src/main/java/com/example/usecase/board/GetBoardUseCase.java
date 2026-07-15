package com.example.usecase.board;

import com.example.domain.board.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetBoardUseCase {

    private final BoardStorage boardStorage;

    public Board getBoard() {
        return boardStorage.getBoard();
    }
}
