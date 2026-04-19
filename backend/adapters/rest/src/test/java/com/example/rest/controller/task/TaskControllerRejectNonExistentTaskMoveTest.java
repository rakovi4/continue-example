package com.example.rest.controller.task;

import com.example.domain.exception.TaskNotFoundException;
import com.example.usecase.task.MoveTaskRequest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("1.1 Reject move of non-existent task")
@WebMvcTest(TaskController.class)
class TaskControllerRejectNonExistentTaskMoveTest extends AbstractTaskControllerTest {

    private static final UUID NON_EXISTENT_TASK_ID = UUID.fromString("00000000-0000-0000-0000-000000000099");

    @Test
    @SneakyThrows
    @DisplayName("""
            When the user moves a non-existent task to a column
            Then the move is rejected with a not found error""")
    void should_return_not_found_for_non_existent_task() {
        doThrow(new TaskNotFoundException("Task not found"))
                .when(moveTaskUseCase)
                .moveTask(eq(new MoveTaskRequest(NON_EXISTENT_TASK_ID, 1001L, 0)));

        var result = mockMvc.perform(patch("/api/v1/tasks/{id}", NON_EXISTENT_TASK_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"columnId\": 1001, \"position\": 0}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("TASK_NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Task not found"))
                .andReturn();

        assertTimestampIsNow(result);
    }
}
