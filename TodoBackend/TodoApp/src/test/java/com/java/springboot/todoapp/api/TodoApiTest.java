package com.java.springboot.todoapp.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TodoApiTest {

    @LocalServerPort
    private int port;

    private String token;  // <-- private et non static

    @BeforeAll
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;

        Map<String, String> registerBody = Map.of(
                "email", "testuser@example.com",
                "password", "1234"
        );

        // Register
        given()
                .contentType(ContentType.JSON)
                .body(registerBody)
                .when()
                .post("/auth/register");

        // Login
        Response response = given()
                .contentType(ContentType.JSON)
                .body(registerBody)
                .when()
                .post("/auth/login");

        token = response.jsonPath().getString("token");
    }

    @Test
    void testCreateTodo() {
        Map<String, Object> todoBody = Map.of(
                "title", "Test Todo RestAssured",
                "isCompleted", false
        );

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(todoBody)
                .when()
                .post("/api/todo/create")
                .then()
                .statusCode(201)
                .body("title", equalTo("Test Todo RestAssured"))
                .body("isCompleted", equalTo(false));
    }
}