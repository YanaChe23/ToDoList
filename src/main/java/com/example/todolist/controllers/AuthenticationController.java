package com.example.todolist.controllers;

import com.example.todolist.api.v1.dto.AuthRequestDto;
import com.example.todolist.services.auth.AuthenticationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationServiceImpl authenticationService;

    @PostMapping("/authenticate")
    public ResponseEntity<String> register(@RequestBody AuthRequestDto authRequestDto) {
        return ResponseEntity.ok(authenticationService.authenticate(authRequestDto));
    }
}