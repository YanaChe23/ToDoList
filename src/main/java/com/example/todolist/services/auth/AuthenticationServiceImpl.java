package com.example.todolist.services.auth;

import com.example.todolist.api.v1.dto.AuthRequestDto;
import com.example.todolist.api.v1.dto.AuthResponseDto;
import com.example.todolist.services.jwt.JwtServiceImpl;
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
    private final JwtServiceImpl jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponseDto authenticate(AuthRequestDto authRequestDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequestDto.getEmail(), authRequestDto.getPassword())
        );
        var userToAuthenticate = userRepository.findByEmail(authRequestDto.getEmail()).orElseThrow(()
                -> new ItemNotFoundException("Can't find a user with an email " + authRequestDto.getEmail()));
        return new AuthResponseDto()
                .token(
                        jwtService.generateToken(userToAuthenticate)
                );
    }
}
