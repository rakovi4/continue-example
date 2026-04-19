package com.example.domain.board;

import com.example.domain.exception.TaskNotFoundException;
import com.example.domain.task.Description;
import com.example.domain.task.Task;
import com.example.domain.task.Title;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.example.domain.board.ColumnType.TO_DO;

@Getter
@RequiredArgsConstructor
public class Board {

    private final List<Column> columns;

    public Task addTask(Title title, Description description) {
        return findToDoColumn().addTask(title, description);
    }

    public void moveTask(UUID taskId, long columnId, int position) {
        rejectNonExistentTask(taskId);
    }

    private void rejectNonExistentTask(UUID taskId) {
        if (findTask(taskId).isEmpty()) {
            throw new TaskNotFoundException("Task not found");
        }
    }

    private Optional<Task> findTask(UUID taskId) {
        return columns.stream()
                .flatMap(column -> column.getTasks().stream())
                .filter(task -> task.getId().equals(taskId))
                .findFirst();
    }

    public Column findToDoColumn() {
        return columns.stream()
                .filter(column -> column.getType() == TO_DO)
                .findFirst()
                .orElseThrow();
    }
}
