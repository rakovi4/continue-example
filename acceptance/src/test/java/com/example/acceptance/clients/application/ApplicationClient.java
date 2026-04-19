package com.example.acceptance.clients.application;

import com.example.acceptance.clients.application.dto.ErrorResponse;
import com.example.acceptance.clients.application.dto.board.BoardResponse;
import com.example.acceptance.clients.application.dto.board.ColumnResponse;
import com.example.acceptance.clients.application.dto.task.CreateTaskRequest;
import com.example.acceptance.clients.application.dto.task.MoveTaskRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
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

    public Response moveTask(long taskId, MoveTaskRequest request) {
        return RestAssured
                .given()
                .baseUri(baseUrl)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .patch("/api/v1/tasks/" + taskId)
                .then()
                .extract()
                .response();
    }

    public Response createTask(CreateTaskRequest request) {
        return RestAssured
                .given()
                .baseUri(baseUrl)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/tasks")
                .then()
                .extract()
                .response();
    }
}
