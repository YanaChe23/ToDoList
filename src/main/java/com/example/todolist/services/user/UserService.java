package com.example.todolist.services.user;

import com.example.todolist.entities.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    void saveUser(User user);
    User getUser(int id);
    void deleteUser(int id);
    void deleteAllUsers();
    List<User> findAllUsers();
}
