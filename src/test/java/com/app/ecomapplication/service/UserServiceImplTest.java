package com.app.ecomapplication.service;

import com.app.ecomapplication.model.User;
import com.app.ecomapplication.repository.UserRepository;
import com.app.ecomapplication.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    private final String apiUrl =
            "https://fakestoreapi.com/users";

    @BeforeEach
    void setup() {

        ReflectionTestUtils.setField(
                userService,
                "apiUrl",
                apiUrl
        );

        user = new User();
        user.setId(1L);
        user.setUsername("johnd");
        user.setPassword("m38rmF$");
        user.setEmail("john@test.com");
    }

    @Test
    void shouldReturnUsers() {

        when(userRepository.findAll())
                .thenReturn(List.of(user));

        List<User> users =
                userService.getAllUsers();

        assertEquals(1, users.size());
    }

    @Test
    void shouldReturnUserById() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        User result =
                userService.getUserById(1L);

        assertEquals("johnd",
                result.getUsername());
    }

    @Test
    void shouldAddUser() {

        when(userRepository.save(user))
                .thenReturn(user);

        User saved =
                userService.addUser(user);

        assertNotNull(saved);
    }

    @Test
    void shouldDeleteUser() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        userService.deleteUser(1L);

        verify(userRepository, times(1))
                .delete(user);
    }

    @Test
    void shouldSyncUsersFromApi() {

        User[] apiUsers = { user };

        when(restTemplate.getForObject(
                apiUrl,
                User[].class
        )).thenReturn(apiUsers);

        when(userRepository.saveAll(anyList()))
                .thenReturn(List.of(user));

        List<User> users =
                userService.syncUsersFromApi();

        assertEquals(1, users.size());
    }
}