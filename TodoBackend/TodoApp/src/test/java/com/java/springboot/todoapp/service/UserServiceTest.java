package com.java.springboot.todoapp.service;

import com.java.springboot.todoapp.models.User;
import com.java.springboot.todoapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser_shouldReturnSavedUser() {
        // GIVEN
        User user = new User(1L, "test@mail.com", "123456");
        when(userRepository.save(user)).thenReturn(user);

        // WHEN
        User result = userService.createUser(user);

        // THEN
        assertNotNull(result);
        assertEquals("test@mail.com", result.getEmail());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void getUserById_shouldReturnUser() {
        // GIVEN
        User user = new User(1L, "test@mail.com", "123456");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.getUserById(1L);

        // THEN
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getUserById_shouldThrowExceptionIfNotFound() {
        // GIVEN
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        // THEN
        assertThrows(RuntimeException.class,
                () -> userService.getUserById(2L));
    }
}
