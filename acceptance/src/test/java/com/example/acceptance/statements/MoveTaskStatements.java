package com.example.acceptance.statements;

import com.example.acceptance.clients.application.ApplicationClient;
import com.example.acceptance.clients.application.dto.task.MoveTaskRequest;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.example.acceptance.statements.AssertionHelpers.assertErrorResponse;

@Service
@RequiredArgsConstructor
public class MoveTaskStatements {

    private static final String NON_EXISTENT_TASK_ID = UUID.randomUUID().toString();
    private static final long ANY_COLUMN_ID = 1L;

    private final ApplicationClient applicationClient;

    private Response lastResponse;

    public void whenUserMovesNonExistentTask() {
        lastResponse = applicationClient.moveTask(
                NON_EXISTENT_TASK_ID,
                new MoveTaskRequest(ANY_COLUMN_ID, 0)
        );
    }

    public void assertNotFoundError() {
        assertErrorResponse(lastResponse, 404, "TASK_NOT_FOUND", "Task not found");
    }
}
