package com.example.usecase.task;

import com.example.domain.task.Task;
import com.example.usecase.board.BoardStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class CreateTaskUseCase {

    private final BoardStorage boardStorage;
    private final Clock clock;

    public Task createTask(CreateTaskRequest request) {
        var title = request.toTitle();
        var description = request.toDescription();
        var task = boardStorage.getBoard().addTask(title, description, now());
        boardStorage.saveTask(task);
        return task;
    }

    private Instant now() {
        return Instant.now(clock).truncatedTo(ChronoUnit.MILLIS);
    }
}
