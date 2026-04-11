package com.example.h2.access.board;

import com.example.domain.board.Board;
import com.example.domain.board.Column;
import com.example.domain.board.ColumnType;
import com.example.domain.task.Task;
import com.example.h2.entity.TaskEntity;
import com.example.h2.repository.TaskJpaRepository;
import com.example.usecase.board.BoardStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional(propagation = Propagation.MANDATORY)
    public void save(Board board) {
        board.getColumns().forEach(this::saveColumnTasks);
    }

    private void saveColumnTasks(Column column) {
        column.getTasks().forEach(task ->
                taskJpaRepository.save(TaskEntity.from(task, column.getType()))
        );
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
