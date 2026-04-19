package com.example.usecase.task.move;

import com.example.usecase.ApplicationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("1.1 Reject move of non-existent task")
class RejectMoveNonExistentTaskUseCaseTest extends ApplicationTest {

    @Test
    @DisplayName("""
            When the user moves a non-existent task to a column
            Then the move is rejected with a not found error""")
    void should_reject_move_of_non_existent_task() {
        moveTaskStatements.moveNonExistentTask();
        moveTaskStatements.assertTaskNotFoundError();
    }
}
