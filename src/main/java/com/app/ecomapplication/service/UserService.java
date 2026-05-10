package com.app.ecomapplication.service;

import com.app.ecomapplication.model.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User getUserById(Long id);
    List<User> getUsersLimited(int limit);
    List<User> getUsersSorted(String sort);
    User addUser(User user);
    User updateUser(Long id, User user);
    void deleteUser(Long id);
}
