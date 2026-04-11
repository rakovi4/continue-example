package com.example.acceptance.tests.backend;

import com.example.acceptance.clients.application.ApplicationClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@Tag("backend")
@SpringBootTest(classes = AcceptanceTestConfig.class)
@ActiveProfiles("test")
public abstract class AbstractBackendTest {

    @Autowired
    private ApplicationClient applicationClient;

    @BeforeEach
    void cleanup() {
        applicationClient.cleanup();
    }
}
