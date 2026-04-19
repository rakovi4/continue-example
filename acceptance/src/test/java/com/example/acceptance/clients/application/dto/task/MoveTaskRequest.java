package com.example.acceptance.clients.application.dto.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoveTaskRequest {
    private long columnId;
    private int position;
}
