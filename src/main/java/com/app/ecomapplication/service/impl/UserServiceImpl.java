package com.app.ecomapplication.service.impl;
import com.app.ecomapplication.exception.UserNotFoundException;
import com.app.ecomapplication.model.User;
import com.app.ecomapplication.repository.UserRepository;
import com.app.ecomapplication.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;
import java.util.List;
@Service
public class UserServiceImpl implements UserService {

    @Value("${user.fakestore.api.url}")
    private String apiUrl;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    public UserServiceImpl(
            UserRepository userRepository,
            RestTemplate restTemplate
    ) {
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
    }
    @Override
    public List<User> syncUsersFromApi() {
        User[] apiUsers =
                restTemplate.getForObject(
                        apiUrl,
                        User[].class
                );
        if (apiUsers == null) {
            throw new UserNotFoundException(
                    "No users found from API"
            );
        }
        List<User> users = Arrays.asList(apiUsers);
        users.forEach(user -> user.setId(null));
        return userRepository.saveAll(users);
    }
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found"
                        ));
    }
    @Override
    public List<User> getUsersLimited(int limit) {

        return userRepository.findAll()
                .stream()
                .limit(limit)
                .toList();
    }
    @Override
    public List<User> getUsersSorted(String sort) {

        Sort sorting = sort.equalsIgnoreCase("desc")
                ? Sort.by("username").descending()
                : Sort.by("username").ascending();

        return userRepository.findAll(sorting);
    }

    @Override
    public User addUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, User user) {

        User existing =
                userRepository.findById(id)
                        .orElseThrow(() ->
                                new UserNotFoundException(
                                        "User not found"
                                ));

        existing.setEmail(user.getEmail());
        existing.setUsername(user.getUsername());
        existing.setPassword(user.getPassword());
        existing.setName(user.getName());
        existing.setAddress(user.getAddress());
        existing.setPhone(user.getPhone());

        return userRepository.save(existing);
    }
    @Override
    public void deleteUser(Long id) {

        User existing =
                userRepository.findById(id)
                        .orElseThrow(() ->
                                new UserNotFoundException(
                                        "User not found"
                                ));

        userRepository.delete(existing);
    }
}