package com.example.rest.dto.task;

import com.example.usecase.task.CreateTaskRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskRequestDto {

    private String title;
    private String description;

    public CreateTaskRequest toUsecaseRequest() {
        return new CreateTaskRequest(title, description != null ? description : "");
    }
}
