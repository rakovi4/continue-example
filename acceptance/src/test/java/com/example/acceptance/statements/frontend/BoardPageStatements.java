package com.example.acceptance.statements.frontend;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class BoardPageStatements {

    private static final By BOARD = By.cssSelector("[data-testid='board']");
    private static final By COLUMN = By.cssSelector("[data-testid='board-column']");
    private static final By COLUMN_TITLE = By.cssSelector("[data-testid='column-title']");
    private static final By ADD_TASK_BUTTON = By.cssSelector("[data-testid='add-task-button']");
    private static final By TASK_CARD = By.cssSelector("[data-testid='task-card']");

    private final WebDriver driver;
    private final WebDriverWait wait;

    public BoardPageStatements(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public void navigateToBoardPage(String appUrl) {
        driver.get(appUrl);
        wait.until(ExpectedConditions.visibilityOfElementLocated(BOARD));
    }

    public void assertThreeColumnsDisplayed() {
        assertColumnCountAndVisibility();
        assertColumnTitles();
    }

    public void assertAllColumnsEmpty() {
        List<WebElement> columns = driver.findElements(COLUMN);
        for (WebElement column : columns) {
            List<WebElement> tasks = column.findElements(TASK_CARD);
            String columnTitle = column.findElement(COLUMN_TITLE).getText();
            assertThat(tasks).as("column '%s' has no tasks", columnTitle).isEmpty();
        }
    }

    public void assertAddTaskButtonVisible() {
        WebElement addButton = driver.findElement(ADD_TASK_BUTTON);
        assertThat(addButton.isDisplayed()).as("Add Task button is visible").isTrue();
        assertThat(addButton.getText()).as("Add Task button text").isEqualTo("Добавить задачу");
    }

    private void assertColumnCountAndVisibility() {
        List<WebElement> columns = driver.findElements(COLUMN);
        assertThat(columns).as("board has three columns").hasSize(3);
        assertThat(columns.get(0).isDisplayed()).as("first column is visible").isTrue();
        assertThat(columns.get(1).isDisplayed()).as("second column is visible").isTrue();
        assertThat(columns.get(2).isDisplayed()).as("third column is visible").isTrue();
    }

    private void assertColumnTitles() {
        List<WebElement> titles = driver.findElements(COLUMN_TITLE);
        assertThat(titles).as("three column titles visible").hasSize(3);
        assertThat(titles.get(0).isDisplayed()).as("first column title is visible").isTrue();
        assertThat(titles.get(0).getText()).as("first column title").isEqualTo("К выполнению");
        assertThat(titles.get(1).isDisplayed()).as("second column title is visible").isTrue();
        assertThat(titles.get(1).getText()).as("second column title").isEqualTo("В работе");
        assertThat(titles.get(2).isDisplayed()).as("third column title is visible").isTrue();
        assertThat(titles.get(2).getText()).as("third column title").isEqualTo("Готово");
    }
}
