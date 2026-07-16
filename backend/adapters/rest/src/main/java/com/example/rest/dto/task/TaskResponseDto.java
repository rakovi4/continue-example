package com.example.rest.dto.task;

import com.example.domain.task.Task;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
public class TaskResponseDto {

    UUID id;
    String title;
    String description;
    int position;
    Instant createdAt;

    public static TaskResponseDto from(Task task) {
        return new TaskResponseDto(
                task.getId(),
                task.getTitle().getValue(),
                task.getDescription().getValue(),
                task.getPosition(),
                task.getCreatedAt()
        );
    }
}
