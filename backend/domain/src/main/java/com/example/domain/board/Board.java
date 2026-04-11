package com.example.domain.board;

import com.example.domain.task.Description;
import com.example.domain.task.Task;
import com.example.domain.task.Title;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.example.domain.board.ColumnType.TO_DO;

@Getter
@RequiredArgsConstructor
public class Board {

    private final List<Column> columns;

    public Task addTask(Title title, Description description) {
        return findToDoColumn().addTask(title, description);
    }

    public Column findToDoColumn() {
        return columns.stream()
                .filter(column -> column.getType() == TO_DO)
                .findFirst()
                .orElseThrow();
    }
}
