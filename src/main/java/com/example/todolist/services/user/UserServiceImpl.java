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
    public List<User> get() {
        return userRepository.findAll();
    }
    @Override
    public User save(User user) {
        return userRepository.save(user);
    }
    @Override
    public User findById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
    public void deleteAll() {
        userRepository.deleteAll();
    }

}
