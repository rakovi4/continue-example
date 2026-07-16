package com.example.rest.controller.task;

import com.example.domain.task.Task;
import com.example.domain.task.Title;
import com.example.rest.RestTest;
import com.example.usecase.task.CreateTaskRequest;
import com.example.usecase.task.CreateTaskUseCase;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("3.1 Create task with title only")
@WebMvcTest(TaskController.class)
class TaskControllerCreateTaskWithTitleTest implements RestTest {

    private static final UUID TASK_ID = UUID.fromString("b3f1c2d4-5e6a-47b8-9c0d-1e2f3a4b5c6d");
    private static final Instant CREATED_AT = Instant.parse("2026-07-16T10:15:30.123Z");

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreateTaskUseCase createTaskUseCase;

    @Test
    @SneakyThrows
    @DisplayName("""
            When the user creates a task with title "Set up CI/CD"
            Then the task is created successfully
            And the response contains the task with title "Set up CI/CD" and no description
            And the task has a position and creation timestamp""")
    void should_return_created_task_with_title_only() {
        when(createTaskUseCase.createTask(eq(new CreateTaskRequest("Set up CI/CD"))))
                .thenReturn(new Task(TASK_ID, new Title("Set up CI/CD"), null, 1, CREATED_AT));

        String expectedJson = new String(
                getClass().getResourceAsStream("/task/create-task-with-title.json").readAllBytes()
        );

        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"Set up CI/CD\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().json(expectedJson, true));
    }
}
