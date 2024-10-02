package com.example.demo.service;

import com.example.demo.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.demo.entity.User;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    // Method to validate user login data
    public boolean validateLogin(User user) {
        Optional<User> user1 = userRepository.findUserByUsername(user.getUsername());
        if (user1.get().getUsername().equals(user.getUsername())) {
            return passwordEncoder.matches(user.getPassword(), user1.get().getPassword());
        }
        return false;
    }

    public boolean addUser(String username, String role, String password) {
        try {
            User user = new User(username, role, passwordEncoder.encode(password));
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean registerUser(String username, String password, String role) {
        if (userRepository.findUserByUsername(username) != null) {
            return false;
        }
        String encodedPassword = passwordEncoder.encode(password);

        User user = new User(username, encodedPassword, role);
        userRepository.save(user);

        return true;
    }

}
