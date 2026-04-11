package com.example.domain.board;

import com.example.domain.exception.DuplicateTitleException;
import com.example.domain.task.Description;
import com.example.domain.task.Task;
import com.example.domain.task.Title;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class Column {

    private final ColumnType type;
    private final List<Task> tasks;

    public static Column empty(ColumnType type) {
        return new Column(type, new ArrayList<>());
    }

    public String getName() {
        return type.value();
    }

    public Task addTask(Title title, Description description) {
        rejectDuplicateTitle(title);
        var task = Task.create(title, description, tasks.size());
        tasks.add(task);
        return task;
    }

    private void rejectDuplicateTitle(Title title) {
        if (tasks.stream().anyMatch(task -> task.getTitle().equals(title))) {
            throw new DuplicateTitleException();
        }
    }
}
