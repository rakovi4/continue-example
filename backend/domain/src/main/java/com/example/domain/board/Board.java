package com.example.domain.board;

import com.example.domain.task.Description;
import com.example.domain.task.Task;
import com.example.domain.task.Title;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class Board {

    private final List<Column> columns;

    public Task addTask(Title title, Description description, Instant createdAt) {
        List<Task> toDoTasks = findToDoColumn().getTasks();
        Task task = new Task(UUID.randomUUID(), title, description, bottomPosition(toDoTasks), createdAt);
        toDoTasks.add(task);
        return task;
    }

    private Column findToDoColumn() {
        return columns.stream()
                .filter(column -> column.getType() == ColumnType.TO_DO)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Board has no To Do column"));
    }

    private int bottomPosition(List<Task> tasks) {
        return tasks.size() + 1;
    }
}
