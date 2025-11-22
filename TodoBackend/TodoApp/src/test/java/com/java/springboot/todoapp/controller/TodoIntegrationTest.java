package com.java.springboot.todoapp.controller;

import com.java.springboot.todoapp.models.Todo;
import com.java.springboot.todoapp.models.User;
import com.java.springboot.todoapp.repository.TodoRepository;
import com.java.springboot.todoapp.repository.UserRepository;
import com.java.springboot.todoapp.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TodoListIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private String token;
    private String baseUrl;

    @BeforeEach
    void setUp() {
        // Nettoyer les données avant chaque test
        todoRepository.deleteAll();
        userRepository.deleteAll();

        // Créer un utilisateur de test
        User user = User.builder().email("test@test.com").password("password").build();
        userRepository.save(user);

        // Générer le token JWT
        token = jwtUtil.generateToken(user.getEmail());

        baseUrl = "http://localhost:" + port + "/api/todo";
    }

    private HttpHeaders getAuthHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    @Test
    void testCreateAndGetTodo() {
        // Création d’un todo
        Todo todo = new Todo();
        todo.setTitle("Integration Test Todo");
        todo.setIsCompleted(false);

        HttpEntity<Todo> request = new HttpEntity<>(todo, getAuthHeaders());
        ResponseEntity<Todo> response = restTemplate.postForEntity(baseUrl + "/create", request, Todo.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Integration Test Todo", response.getBody().getTitle());

        Long todoId = response.getBody().getId();

        // Vérifier la récupération du todo
        HttpEntity<Void> getRequest = new HttpEntity<>(getAuthHeaders());
        ResponseEntity<Todo> getResponse = restTemplate.exchange(baseUrl + "/" + todoId, HttpMethod.GET, getRequest, Todo.class);
        assertEquals(HttpStatus.CREATED, getResponse.getStatusCode());
        assertEquals("Integration Test Todo", getResponse.getBody().getTitle());
    }

    @Test
    void testUpdateTodo() {
        // Créer un todo
        Todo todo = new Todo();
        todo.setTitle("Todo to update");
        todo.setIsCompleted(false);
        todo = todoRepository.save(todo);

        // Mettre à jour le todo
        todo.setIsCompleted(true);
        HttpEntity<Todo> request = new HttpEntity<>(todo, getAuthHeaders());
        ResponseEntity<Todo> response = restTemplate.exchange(baseUrl, HttpMethod.PUT, request, Todo.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getBody().getIsCompleted());
    }

    @Test
    void testDeleteTodo() {
        // Créer un todo
        Todo todo = new Todo();
        todo.setTitle("Todo to delete");
        todo.setIsCompleted(false);
        todo = todoRepository.save(todo);

        // Supprimer le todo
        HttpEntity<Void> request = new HttpEntity<>(getAuthHeaders());
        ResponseEntity<Void> response = restTemplate.exchange(baseUrl + "/" + todo.getId(), HttpMethod.DELETE, request, Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(todoRepository.findById(todo.getId()).isPresent());
    }
}
