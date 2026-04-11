package com.example.h2.entity;

import com.example.domain.board.ColumnType;
import com.example.domain.task.Description;
import com.example.domain.task.Task;
import com.example.domain.task.Title;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "tasks")
@Getter
@NoArgsConstructor
public class TaskEntity {

    @Id
    private UUID id;

    private String title;

    private String description;

    private int position;

    private Instant createdAt;

    @Enumerated(EnumType.STRING)
    private ColumnType columnType;

    public static TaskEntity from(Task task, ColumnType columnType) {
        var entity = new TaskEntity();
        entity.id = task.getId();
        entity.title = task.getTitle().getValue();
        entity.description = task.getDescription().getValue();
        entity.position = task.getPosition();
        entity.createdAt = task.getCreatedAt();
        entity.columnType = columnType;
        return entity;
    }

    public Task toDomain() {
        return new Task(
                id,
                new Title(title),
                new Description(description),
                position,
                createdAt
        );
    }
}
