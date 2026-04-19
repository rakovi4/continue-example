package com.example.acceptance.statements.frontend;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.assertj.core.api.Assertions.assertThat;

public class TaskCreationFormStatements extends AbstractPageStatements {

    private static final By TASK_CREATION_FORM = By.cssSelector("[data-testid='task-creation-form']");
    private static final By TITLE_FIELD = By.cssSelector("[data-testid='task-title-input']");
    private static final By DESCRIPTION_FIELD = By.cssSelector("[data-testid='task-description-input']");
    private static final By SUBMIT_BUTTON = By.cssSelector("[data-testid='task-submit-button']");

    public TaskCreationFormStatements(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public void assertFormIsDisplayed() {
        WebElement form = wait.until(ExpectedConditions.visibilityOfElementLocated(TASK_CREATION_FORM));
        assertThat(form.isDisplayed()).as("task creation form is displayed").isTrue();
    }

    public void assertTitleFieldVisible() {
        assertFieldVisibleWithPlaceholder(TITLE_FIELD, "title field", "Название задачи");
    }

    public void assertDescriptionFieldVisible() {
        assertFieldVisibleWithPlaceholder(DESCRIPTION_FIELD, "description field", "Описание задачи");
    }

    private void assertFieldVisibleWithPlaceholder(By locator, String fieldName, String expectedPlaceholder) {
        WebElement field = driver.findElement(locator);
        assertThat(field.isDisplayed()).as("%s is visible", fieldName).isTrue();
        assertThat(field.getAttribute("placeholder")).as("%s placeholder", fieldName).isEqualTo(expectedPlaceholder);
    }

    public void assertSubmitButtonVisible() {
        WebElement submitButton = driver.findElement(SUBMIT_BUTTON);
        assertThat(submitButton.isDisplayed()).as("submit button is visible").isTrue();
        assertThat(submitButton.getText()).as("submit button text").isEqualTo("Создать задачу");
    }
}
