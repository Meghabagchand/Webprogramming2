package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean addUser(String name, String email, String username, String password) {
        User user = new User();
        String encodedPassword = passwordEncoder.encode(password);
        user.setName(name);
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(encodedPassword); // Ideally, hash the password before saving
        userRepository.save(user);
        return true;
    }

    public boolean validateUser(String username, String password) {
        User user = userRepository.findByUsername(username);
        String encodedPassword = passwordEncoder.encode(password);

        return user != null && user.getPassword().equals(encodedPassword); // Use hashed password comparison
    }

    public boolean checkIfUserExists(String username) {
        return userRepository.findByUsername(username) != null;
    }
}
