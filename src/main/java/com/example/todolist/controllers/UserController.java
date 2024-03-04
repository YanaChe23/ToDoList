package com.example.todolist.controllers;

import com.example.todolist.api.v1.dto.UserRequestDto;
import com.example.todolist.api.v1.dto.UserResponseDto;
import com.example.todolist.api.v1.rest.UsersApi;
import com.example.todolist.services.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController implements UsersApi {
    private final UserService userService;

    @Override
    public ResponseEntity<UserResponseDto> usersNewPost(UserRequestDto userRequestDto) {
        return ResponseEntity.ok(
                userService.save(userRequestDto)
        );
    }
}