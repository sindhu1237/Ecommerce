package com.app.ecomapplication.service;

import com.app.ecomapplication.model.User;
import com.app.ecomapplication.repository.UserRepository;
import com.app.ecomapplication.security.JwtUtil;
import com.app.ecomapplication.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void shouldGenerateJwtToken() {

        User user = new User();

        user.setUsername("johnd");
        user.setPassword("m38rmF$");

        when(userRepository.findByUsername("johnd"))
                .thenReturn(Optional.of(user));

        when(jwtUtil.generateToken("johnd"))
                .thenReturn("jwt-token");

        String token =
                authService.login(
                        "johnd",
                        "m38rmF$"
                );

        assertNotNull(token);

        assertEquals("jwt-token", token);

        verify(jwtUtil, times(1))
                .generateToken("johnd");
    }

    @Test
    void shouldThrowExceptionForInvalidPassword() {

        User user = new User();

        user.setUsername("johnd");
        user.setPassword("wrong-password");

        when(userRepository.findByUsername("johnd"))
                .thenReturn(Optional.of(user));

        Exception exception =
                assertThrows(
                        RuntimeException.class,
                        () -> authService.login(
                                "johnd",
                                "m38rmF$"
                        )
                );

        assertTrue(
                exception.getMessage()
                        .contains("Invalid password")
        );
    }
}