package com.example.todolist.services.user;

import com.example.todolist.entities.User;

import java.util.List;

public interface UserService {
    List<User> get();
    User save(User user);
    User findById(Long id);
    void deleteById(Long id);
}
