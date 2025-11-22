package com.java.springboot.todoapp.controller;

import com.java.springboot.todoapp.models.Todo;
import com.java.springboot.todoapp.service.TodoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TodoControllerTest {

    @Mock
    private TodoService todoService;

    @InjectMocks
    private TodoController todoController;

    @Test
    void testGetTodoById() {
        Todo todo = new Todo();
        todo.setId(1L);

        Mockito.when(todoService.getTodoById(1L)).thenReturn(todo);

        ResponseEntity<Todo> response = todoController.getTodoById(1L);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void testCreateTodo() {
        Todo todo = new Todo();
        todo.setTitle("work");

        Mockito.when(todoService.createTodo(todo)).thenReturn(todo);

        ResponseEntity<Todo> res = todoController.createUser(todo);

        assertEquals(201, res.getStatusCodeValue());
    }
}
