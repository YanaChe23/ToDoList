package com.example.todolist.services.user;

import com.example.todolist.api.v1.dto.UserRequestDto;
import com.example.todolist.api.v1.dto.UserResponseDto;
import com.example.todolist.entities.Role;
import com.example.todolist.entities.Task;
import com.example.todolist.entities.User;
import com.example.todolist.exceptions.ItemNotFoundException;
import com.example.todolist.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    @Override
    public List<User> get() {
        return userRepository.findAll();
    }
    @Override
    public UserResponseDto save(UserRequestDto userRequestDto) {
        User userToSave = modelMapper.map(userRequestDto, User.class);
        userToSave.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        userToSave.setRole(Role.USER);
        return modelMapper.map(
                userRepository.save(userToSave),
                UserResponseDto.class
        );
    }
    @Override
    public User findById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        optionalUser.orElseThrow(() -> new ItemNotFoundException("Can't find a user with an id " + id));
        return optionalUser.get();
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User findByEmail(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        optionalUser.orElseThrow(() -> new ItemNotFoundException("User with email "  + email + " not found."));
        return optionalUser.get();
    }
}