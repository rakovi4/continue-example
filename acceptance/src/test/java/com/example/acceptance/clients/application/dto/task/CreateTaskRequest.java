package com.example.acceptance.clients.application.dto.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskRequest {
    private String title;
    private String description;

    public CreateTaskRequest(String title) {
        this.title = title;
    }
}
