package com.example.usecase.task;

import com.example.domain.task.Description;
import com.example.domain.task.Title;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class CreateTaskRequest {

    private final String title;
    private final String description;

    public CreateTaskRequest(String title) {
        this(title, "");
    }

    public CreateTaskRequest(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Title toTitle() {
        return new Title(title);
    }

    public Description toDescription() {
        return new Description(description);
    }
}
