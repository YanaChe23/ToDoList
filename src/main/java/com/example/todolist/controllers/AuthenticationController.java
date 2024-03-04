package com.example.todolist.controllers;

import com.example.todolist.api.v1.dto.AuthRequestDto;
import com.example.todolist.api.v1.dto.AuthResponseDto;
import com.example.todolist.api.v1.rest.AuthApi;
import com.example.todolist.services.auth.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationController implements AuthApi {
    private final AuthenticationService authenticationService;

    @Override
    public ResponseEntity<AuthResponseDto> authAuthenticatePost(AuthRequestDto authRequestDto) {
        return ResponseEntity.ok(authenticationService.authenticate(authRequestDto));
    }
}