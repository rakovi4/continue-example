package com.example.usecase.task;

import com.example.domain.task.Task;
import com.example.usecase.board.BoardStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;

@Service
@RequiredArgsConstructor
public class CreateTaskUseCase {

    private final BoardStorage boardStorage;
    private final Clock clock;

    public Task createTask(CreateTaskRequest request) {
        request.toTitle();
        request.toDescription();
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
