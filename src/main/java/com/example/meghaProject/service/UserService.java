package com.example.meghaProject.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.meghaProject.model.User;
import com.example.meghaProject.model.User.Role;
import com.example.meghaProject.repo.UserRepository;

@Service
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    // logger implementation
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(8);

    public boolean addUser(String username, String password, Role role) {

        // if (checkIfUserExists(user.getUsername()))
        // return false;

        logger.info("Add User: ", username);
        User user = new User();
        user.setUsername(username);
        user.setPassword(encoder.encode(password));
        user.setRole(role);
        userRepository.save(user);
        return true;
    }

    public String getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = null;
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return username = userDetails.getUsername();
        } else {
            return username = authentication.getName();
        }
    }

    public Object getUserById(Long id) {
        return userRepository.findById(id).orElse(null);

    }

    // get all the user from the database
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean checkIfUserExists(String username) {
        return userRepository.findByUsername(username) != null;
    }

    public Role checkRole(Role role) {
        User user = userRepository.findByUsername(getUser());
        return user.getRole();

    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

}
