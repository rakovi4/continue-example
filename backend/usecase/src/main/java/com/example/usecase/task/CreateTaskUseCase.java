package com.example.usecase.task;

import com.example.domain.task.Task;
import com.example.usecase.board.BoardStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateTaskUseCase {

    private final BoardStorage boardStorage;

    @Transactional
    public Task createTask(CreateTaskRequest request) {
        var board = boardStorage.getBoard();
        var task = board.addTask(request.toTitle(), request.toDescription());
        boardStorage.save(board);
        return task;
    }
}
