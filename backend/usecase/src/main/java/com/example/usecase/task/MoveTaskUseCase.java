package com.example.usecase.task;

import com.example.usecase.board.BoardStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MoveTaskUseCase {

    private final BoardStorage boardStorage;

    public void moveTask(MoveTaskRequest request) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
