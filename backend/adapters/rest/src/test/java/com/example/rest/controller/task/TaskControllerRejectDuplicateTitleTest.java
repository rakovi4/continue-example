package com.example.rest.controller.task;

import com.example.domain.exception.DuplicateTitleException;
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

@DisplayName("5.1 Reject duplicate task title")
@WebMvcTest(TaskController.class)
class TaskControllerRejectDuplicateTitleTest implements RestTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreateTaskUseCase createTaskUseCase;

    @Test
    @SneakyThrows
    @DisplayName("""
            Given a task "Set up CI/CD" exists in To Do
            When the user creates a task with title "Set up CI/CD"
            Then the task is rejected with a duplicate title error""")
    void should_reject_task_with_duplicate_title() {
        doThrow(new DuplicateTitleException())
                .when(createTaskUseCase).createTask(eq(new CreateTaskRequest("Set up CI/CD")));

        var result = mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"Set up CI/CD\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("DUPLICATE_TITLE"))
                .andExpect(jsonPath("$.message").value("A task with this title already exists"))
                .andReturn();

        assertTimestampIsNow(result);
    }
}
