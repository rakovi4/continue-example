package com.example.acceptance.clients.application;

import com.example.acceptance.clients.application.dto.board.BoardResponse;
import com.example.acceptance.clients.application.dto.board.ColumnResponse;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public BoardResponse getBoard() {
        Response response = RestAssured
                .given()
                .baseUri(baseUrl)
                .when()
                .get("/api/v1/board")
                .then()
                .extract()
                .response();

        List<ColumnResponse> columns = response.jsonPath().getList("columns", ColumnResponse.class);
        return new BoardResponse(columns);
    }
}
