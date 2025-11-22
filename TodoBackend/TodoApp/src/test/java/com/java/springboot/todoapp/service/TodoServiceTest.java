package com.java.springboot.todoapp.service;

import com.java.springboot.todoapp.models.Todo;
import com.java.springboot.todoapp.repository.TodoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoService todoService;

    @Test
    void testCreateTodo() {
        Todo todo = new Todo();
        todo.setTitle("Learn Spring");

        Mockito.when(todoRepository.save(todo)).thenReturn(todo);

        Todo result = todoService.createTodo(todo);

        Assertions.assertEquals("Learn Spring", result.getTitle());
    }

    @Test
    void testGetTodoById() {
        Todo todo = new Todo();
        todo.setId(1L);

        Mockito.when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));

        Todo result = todoService.getTodoById(1L);

        Assertions.assertEquals(1L, result.getId());
    }

    @Test
    void testGetTodoById_NotFound() {
        Mockito.when(todoRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(RuntimeException.class,
                () -> todoService.getTodoById(1L));
    }


    @Test
    void testUpdateTodo() {
        Todo todo = new Todo();
        todo.setTitle("Updated");

        Mockito.when(todoRepository.save(todo)).thenReturn(todo);

        Todo result = todoService.updateTodoById(todo);

        Assertions.assertEquals("Updated", result.getTitle());
    }

    @Test
    void testDeleteTodoById() {
        Todo todo = new Todo();
        todo.setId(1L);

        Mockito.when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));

        todoService.deleteTodoById(1L);

        Mockito.verify(todoRepository).delete(todo);
    }
}
