package com.example.acceptance.tests.frontend;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

@Tag("frontend")
public abstract class AbstractUiTest {

    protected WebDriver webDriver;
    protected WebDriverWait wait;
    protected String appUrl;

    @BeforeEach
    void setUpWebDriver() {
        appUrl = resolveAppUrl();
        webDriver = createHeadlessChromeDriver();
        wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
    }

    private String resolveAppUrl() {
        String frontendUrl = System.getenv("FRONTEND_URL");
        if (frontendUrl != null) {
            return frontendUrl;
        }
        String port = System.getenv().getOrDefault("FRONTEND_PORT", "5173");
        return "http://localhost:" + port;
    }

    private ChromeDriver createHeadlessChromeDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        return new ChromeDriver(options);
    }

    @AfterEach
    void tearDownWebDriver() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }
}
