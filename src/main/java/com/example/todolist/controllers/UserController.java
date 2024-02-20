package com.example.todolist.controllers;

import com.example.todolist.api.v1.dto.UserRequestDto;
import com.example.todolist.api.v1.dto.UserResponseDto;
import com.example.todolist.api.v1.rest.UsersApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController implements UsersApi {

    @Override
    public ResponseEntity<UserResponseDto> usersPost(UserRequestDto userRequestDto) {
        return ResponseEntity.ok(null);
    }
}
