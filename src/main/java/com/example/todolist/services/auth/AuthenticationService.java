package com.example.todolist.services.auth;

import com.example.todolist.api.v1.dto.AuthRequestDto;


public interface AuthenticationService {
    String authenticate(AuthRequestDto authRequestDto);
}
