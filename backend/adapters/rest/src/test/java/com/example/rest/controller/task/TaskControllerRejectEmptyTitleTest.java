package com.example.rest.controller.task;

import com.example.domain.exception.ValidationException;
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

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("2.1 Reject empty title")
@WebMvcTest(TaskController.class)
class TaskControllerRejectEmptyTitleTest implements RestTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreateTaskUseCase createTaskUseCase;

    @Test
    @SneakyThrows
    @DisplayName("""
            When the user creates a task with an empty title
            Then the task is rejected with a validation error
            And the error message indicates the title is required""")
    void should_reject_task_with_empty_title() {
        doThrow(new ValidationException("Title is required"))
                .when(createTaskUseCase).createTask(eq(new CreateTaskRequest("")));

        var result = mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("Title is required"))
                .andReturn();

        assertTimestampIsNow(result);
    }
}
