package com.java.springboot.todoapp.controller;

import com.java.springboot.todoapp.models.User;
import com.java.springboot.todoapp.repository.UserRepository;
import com.java.springboot.todoapp.service.UserService;
import com.java.springboot.todoapp.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthController authController;

    @Test
    void loginUser_Success() {
        Map<String,String> request = Map.of("email","test@test.com","password","1234");
        User user = User.builder().email("test@test.com").password("encoded").build();

        Mockito.when(userRepository.findByEmail("test@test.com"))
                .thenReturn(Optional.of(user));

        Mockito.when(passwordEncoder.matches("1234","encoded"))
                .thenReturn(true);

        Mockito.when(jwtUtil.generateToken("test@test.com"))
                .thenReturn("TOKEN");

        ResponseEntity<?> response = authController.loginUser(request);

        assertEquals(200, response.getStatusCode().value());    }

    @Test
    void loginUser_InvalidPassword() {
        Map<String,String> request = Map.of("email","test@test.com","password","wrong");

        User user = User.builder().email("test@test.com").password("encoded").build();
        Mockito.when(userRepository.findByEmail("test@test.com"))
                .thenReturn(Optional.of(user));

        Mockito.when(passwordEncoder.matches("wrong","encoded"))
                .thenReturn(false);

        ResponseEntity<?> response = authController.loginUser(request);

        assertEquals(401, response.getStatusCode().value(), "Le code HTTP doit être 401 pour utilisateur non enregistré");
    }
}
