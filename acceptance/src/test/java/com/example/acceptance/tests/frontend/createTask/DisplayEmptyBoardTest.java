package com.example.acceptance.tests.frontend.createTask;

import com.example.acceptance.statements.frontend.BoardPageStatements;
import com.example.acceptance.tests.frontend.AbstractUiTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DisplayEmptyBoardTest extends AbstractUiTest {

    private BoardPageStatements boardPage;

    @BeforeEach
    void setUp() {
        boardPage = new BoardPageStatements(webDriver, wait);
    }

    @Test
    @DisplayName("""
        UI Test Scenario 1.1: Display empty board with three columns
        Given the user opens the board page
        Then three columns are displayed: To Do, In Progress, Done
        And each column is empty
        And the Add Task button is visible
        """)
    void should_display_empty_board_with_three_columns() {

        boardPage.navigateToBoardPage(appUrl);

        boardPage.assertThreeColumnsDisplayed();
        boardPage.assertAllColumnsEmpty();
        boardPage.assertAddTaskButtonVisible();
    }
}
