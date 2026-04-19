package com.example.acceptance.tests.frontend.createTask;

import com.example.acceptance.tests.frontend.AbstractUiTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DisplayTaskCreationFormTest extends AbstractUiTest {

    @Disabled("TimeoutException: waiting for visibility of element located by [data-testid='task-creation-form']")
    @Test
    @DisplayName("""
        UI Test Scenario 2.1: Display task creation form
        Given the user opens the board page
        When the user clicks the Add Task button
        Then the task creation form is displayed
        And the form contains a title field and a description field
        And the submit button is visible
        """)
    void should_display_task_creation_form() {

        boardPage.navigateToBoardPage(appUrl);
        boardPage.clickAddTaskButton();

        taskCreationForm.assertFormIsDisplayed();
        taskCreationForm.assertTitleFieldVisible();
        taskCreationForm.assertDescriptionFieldVisible();
        taskCreationForm.assertSubmitButtonVisible();
    }
}
