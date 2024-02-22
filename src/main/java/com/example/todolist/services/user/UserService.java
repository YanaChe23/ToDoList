package com.example.todolist.services.user;

import com.example.todolist.api.v1.dto.UserRequestDto;
import com.example.todolist.api.v1.dto.UserResponseDto;
import com.example.todolist.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> get();
    UserResponseDto save(UserRequestDto userRequestDto);
    User findById(Long id);
    void deleteById(Long id);
    User findByEmail(String email);
}
