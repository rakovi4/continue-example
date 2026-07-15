package com.example.acceptance.clients.application;

import io.restassured.RestAssured;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ApplicationClient {

    private final String baseUrl;

    public ApplicationClient(@Value("${backend.url}") String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void cleanup() {
        RestAssured
                .given()
                .baseUri(baseUrl)
                .when()
                .post("/api/test/cleanup")
                .then()
                .statusCode(200);
    }
}
