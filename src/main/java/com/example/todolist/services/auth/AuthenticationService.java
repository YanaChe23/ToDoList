package com.example.todolist.services.auth;

import com.example.todolist.api.v1.dto.AuthRequestDto;
import com.example.todolist.api.v1.dto.AuthResponseDto;


public interface AuthenticationService {
    AuthResponseDto authenticate(AuthRequestDto authRequestDto);
}
