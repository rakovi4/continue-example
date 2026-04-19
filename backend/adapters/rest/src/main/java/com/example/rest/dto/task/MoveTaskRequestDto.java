package com.example.rest.dto.task;

import com.example.usecase.task.MoveTaskRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoveTaskRequestDto {

    private long columnId;
    private int position;

    public MoveTaskRequest toUsecaseRequest(UUID taskId) {
        return new MoveTaskRequest(taskId, columnId, position);
    }
}
