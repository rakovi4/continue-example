package com.example.acceptance.statements.frontend;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class AbstractPageStatements {

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected AbstractPageStatements(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }
}
