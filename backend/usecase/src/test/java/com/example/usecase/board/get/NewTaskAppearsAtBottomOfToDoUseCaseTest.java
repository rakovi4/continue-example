package com.example.usecase.board.get;

import com.example.usecase.ApplicationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("4.2 New task appears at bottom of To Do column")
class NewTaskAppearsAtBottomOfToDoUseCaseTest extends ApplicationTest {

    @Test
    @DisplayName("""
            Given a task "First task" exists in To Do
            When the user creates a task with title "Second task"
            Then the task is created successfully
            When the user requests the board
            Then the To Do column contains "First task" before "Second task\"""")
    void should_append_new_task_to_bottom_of_to_do_column() {
        taskStatements.createTaskWithTitleOnly("First task");
        taskStatements.createTaskWithTitleOnly("Second task");
        taskStatements.assertSecondTaskCreated("Second task");
        boardStatements.getBoard();
        boardStatements.assertToDoColumnContainsTasksInOrder("First task", "Second task");
    }
}
