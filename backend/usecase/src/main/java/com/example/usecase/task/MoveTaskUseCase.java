package com.example.usecase.task;

import com.example.usecase.board.BoardStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MoveTaskUseCase {

    private final BoardStorage boardStorage;

    public void moveTask(MoveTaskRequest request) {
        var board = boardStorage.getBoard();
        board.moveTask(request.getTaskId(), request.getColumnId(), request.getPosition());
    }
}
