package com.example.storage.access.board;

import com.example.domain.board.Board;
import com.example.domain.board.Column;
import com.example.domain.board.ColumnType;
import com.example.domain.task.Task;
import com.example.storage.entity.TaskEntity;
import com.example.storage.repository.TaskJpaRepository;
import com.example.usecase.board.BoardStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class H2BoardStorage implements BoardStorage {

    private final TaskJpaRepository taskJpaRepository;

    @Override
    public Board getBoard() {
        var entities = taskJpaRepository.findAll();
        var tasksByColumn = groupByColumn(entities);
        return new Board(buildColumns(tasksByColumn));
    }

    @Override
    public void saveTask(Task task) {
        taskJpaRepository.save(TaskEntity.from(task, ColumnType.TO_DO));
    }

    private Map<ColumnType, List<Task>> groupByColumn(List<TaskEntity> entities) {
        return entities.stream()
                .collect(Collectors.groupingBy(
                        TaskEntity::getColumnType,
                        Collectors.mapping(TaskEntity::toDomain, Collectors.toList())
                ));
    }

    private List<Column> buildColumns(Map<ColumnType, List<Task>> tasksByColumn) {
        return Arrays.stream(ColumnType.values())
                .map(type -> new Column(type, tasksByColumn.getOrDefault(type, new ArrayList<>())))
                .toList();
    }
}
