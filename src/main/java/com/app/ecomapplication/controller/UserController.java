package com.app.ecomapplication.controller;

import com.app.ecomapplication.model.User;
import com.app.ecomapplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public List<com.app.ecomapplication.model.User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/limited")
    public List<User> getUsersLimited(@RequestParam(defaultValue = "5") int limit) {
        return userService.getUsersLimited(limit);
    }

    @GetMapping("/sorted")
    public List<User> getUsersSorted(@RequestParam(defaultValue = "asc") String sort) {
        return userService.getUsersSorted(sort);
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}