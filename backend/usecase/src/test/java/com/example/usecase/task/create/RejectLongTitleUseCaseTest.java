package com.example.usecase.task.create;

import com.example.usecase.ApplicationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("2.2 Reject title exceeding 100 characters")
class RejectLongTitleUseCaseTest extends ApplicationTest {

    @Test
    @DisplayName("""
            When the user creates a task with a title of 101 characters
            Then the task is rejected with a validation error
            And the error message indicates the title is too long""")
    void should_reject_title_exceeding_100_characters() {
        taskStatements.createTaskWithLongTitle(101);
        taskStatements.assertValidationError("Title must not exceed 100 characters");
    }
}
