package com.example.rest.dto.task;

import com.example.domain.task.Description;
import com.example.domain.task.Task;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
public class TaskResponse {

    UUID id;
    String title;
    String description;
    int position;
    Instant createdAt;

    public static TaskResponse from(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle().getValue(),
                descriptionValue(task.getDescription()),
                task.getPosition(),
                task.getCreatedAt()
        );
    }

    private static String descriptionValue(Description description) {
        return description == null ? null : description.getValue();
    }
}
