package com.example.rest.controller.task;

import com.example.domain.task.Description;
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
class TaskControllerCreateTaskTest implements RestTest {

    private static final UUID TASK_ID = UUID.fromString("a1b2c3d4-e5f6-7890-abcd-ef1234567890");
    private static final Instant CREATED_AT = Instant.parse("2026-04-15T10:00:00Z");

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreateTaskUseCase createTaskUseCase;

    @Test
    @SneakyThrows
    @DisplayName("""
            When the user creates a task with title "Set up CI/CD"
            Then the task is created successfully
            And the response contains the task with title and no description
            And the task has a position and creation timestamp""")
    void should_return_created_task_response() {
        when(createTaskUseCase.createTask(eq(new CreateTaskRequest("Set up CI/CD"))))
                .thenReturn(new Task(TASK_ID, new Title("Set up CI/CD"), new Description(""), 0, CREATED_AT));

        String expectedJson = new String(
                getClass().getResourceAsStream("/task/create-task-response.json").readAllBytes()
        );

        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"Set up CI/CD\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().json(expectedJson));
    }
}
