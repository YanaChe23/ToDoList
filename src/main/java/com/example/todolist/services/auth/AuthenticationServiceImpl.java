package com.example.todolist.services.auth;

import com.example.todolist.api.v1.dto.AuthRequestDto;
import com.example.todolist.configs.JWTService;
import com.example.todolist.exceptions.ItemNotFoundException;
import com.example.todolist.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    public String authenticate(AuthRequestDto authRequestDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequestDto.getEmail(), authRequestDto.getPassword())
        );
        var userToAuthenticate = userRepository.findByEmail(authRequestDto.getEmail()).orElseThrow(()
                -> new ItemNotFoundException("Can't find a user with an email " + authRequestDto.getEmail()));
        return jwtService.generateToken(userToAuthenticate);
    }
}
