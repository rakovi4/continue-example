package com.example.acceptance.tests.backend.moveTask;

import com.example.acceptance.statements.MoveTaskStatements;
import com.example.acceptance.tests.backend.AbstractBackendTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("1.1 Reject move of non-existent task")
public class RejectMoveNonExistentTaskTest extends AbstractBackendTest {

    @Autowired
    private MoveTaskStatements moveTaskStatements;

    @Test
    @DisplayName("""
            When the user moves a non-existent task to a column
            Then the move is rejected with a not found error""")
    void should_reject_move_of_non_existent_task() {
        moveTaskStatements.whenUserMovesNonExistentTask();
        moveTaskStatements.assertNotFoundError();
    }
}
