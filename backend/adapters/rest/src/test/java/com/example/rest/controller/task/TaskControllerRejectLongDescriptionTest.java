package com.example.rest.controller.task;

import com.example.domain.exception.ValidationException;
import com.example.usecase.task.CreateTaskRequest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("2.3 Reject description exceeding 5000 characters")
@WebMvcTest(TaskController.class)
class TaskControllerRejectLongDescriptionTest extends AbstractTaskControllerTest {

    @Test
    @SneakyThrows
    @DisplayName("""
            When the user creates a task with a valid title and a description of 5001 characters
            Then the task is rejected with a validation error
            And the error message indicates the description is too long""")
    void should_reject_task_with_description_exceeding_5000_characters() {
        String longDescription = "a".repeat(5001);
        doThrow(new ValidationException("Description must not exceed 5000 characters"))
                .when(createTaskUseCase)
                .createTask(eq(new CreateTaskRequest("Valid title", longDescription)));

        var result = mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"Valid title\", \"description\": \"" + longDescription + "\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("Description must not exceed 5000 characters"))
                .andReturn();

        assertTimestampIsNow(result);
    }
}
