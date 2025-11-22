package com.java.springboot.todoapp.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class TodoApiTest {

    private static String TOKEN;

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8089;

        // Création d'un utilisateur de test et récupération du token JWT
        Map<String, String> registerBody = Map.of(
                "email", "testuser@example.com",
                "password", "1234"
        );

        // On s'assure que l'utilisateur est enregistré (ignore erreur si déjà existant)
        given()
                .contentType(ContentType.JSON)
                .body(registerBody)
                .when()
                .post("/auth/register");

        // Login pour obtenir le token JWT
        Response response = given()
                .contentType(ContentType.JSON)
                .body(registerBody)
                .when()
                .post("/auth/login");

        TOKEN = response.jsonPath().getString("token");
    }

    @Test
    void testCreateTodo() {
        Map<String, Object> todoBody = Map.of(
                "title", "Test Todo RestAssured",
                "isCompleted", false
        );

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + TOKEN)
                .body(todoBody)
                .when()
                .post("/api/todo/create")
                .then()
                .statusCode(201)
                .body("title", equalTo("Test Todo RestAssured"))
                .body("isCompleted", equalTo(false));
    }
}
