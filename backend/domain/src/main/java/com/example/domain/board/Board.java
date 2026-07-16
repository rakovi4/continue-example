package com.example.domain.board;

import com.example.domain.task.Description;
import com.example.domain.task.Task;
import com.example.domain.task.Title;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class Board {

    private final List<Column> columns;

    public Task addTask(Title title, Description description, Instant createdAt) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
