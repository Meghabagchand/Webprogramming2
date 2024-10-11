package com.example.meghaProject.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.meghaProject.model.User;
import com.example.meghaProject.model.User.Role;
import com.example.meghaProject.repo.UserRepository;

@Service
public class DashboardService {

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(8);

    public boolean createUser(String username, String password) {
        logger.info("Add User: ", username);

        User user = new User();
        user.setUsername(username);
        user.setPassword(encoder.encode(password));
        // user.setPassword(password);
        user.setRole(Role.ROLE_USER);
        userRepository.save(user);
        return true;
    }

}
