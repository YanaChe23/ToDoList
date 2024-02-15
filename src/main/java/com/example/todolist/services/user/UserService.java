package com.example.todolist.services.user;

import com.example.todolist.entities.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User saveUser(User user);
    User getUser(Long id);
    void deleteUser(Long id);
    void deleteAllUsers();
    List<User> findAllUsers();
}
