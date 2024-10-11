package com.example.meghaProject;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import com.example.meghaProject.controller.UserController;
import com.example.meghaProject.model.User;
import com.example.meghaProject.model.User.Role;
import com.example.meghaProject.service.UserService;

public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetLoginPage() {
        String viewName = userController.getLoginPage(model);
        assertEquals("login", viewName);
    }

    @Test
    public void testLogin_UserExists() {
        User user = new User();
        user.setUsername("existingUser");
        user.setPassword("password123");

        when(userService.checkIfUserExists(user.getUsername())).thenReturn(true);

        String viewName = userController.login(user, model);
        verify(model).addAttribute("registrationError", "Username already exists.");
        assertEquals("redirect:/dashboard", viewName);
    }

    @Test
    public void testLogin_UserDoesNotExist() {
        User user = new User();
        user.setUsername("newUser");
        user.setPassword("password123");
        when(userService.checkIfUserExists(user.getUsername())).thenReturn(false);

        String viewName = userController.login(user, model);
        verify(userService).addUser(user.getUsername(), user.getPassword(), Role.ROLE_USER);
        verify(model).addAttribute("message", "Registration successful! Please log in.");
        assertEquals("redirect:/", viewName);
    }
}