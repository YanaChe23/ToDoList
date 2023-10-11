package com.example.todolist.services.user;

import com.example.todolist.entities.User;
import com.example.todolist.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }
    @Override
    public User getUser(int id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) return user.get();
        return null;
    }
    @Override
    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

    @Override
    public void deleteAllUsers() {
        userRepository.deleteAll();
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
}
