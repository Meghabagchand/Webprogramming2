package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class LoginController {

    @Autowired
    UserService userService;

    // Home page
    @GetMapping("/")
    public String hello() {
        return "hello";
    }

    // Show login form
    @GetMapping("/login")
    public String showLoginForm(Model model) {
      //  model.addAttribute("user", new User());
        return "login";
    }

//    // Process login
//    @PostMapping("/login")
//    public String processLogin(@ModelAttribute("user") User user, Model model) {
//        if (userService.validateUser(user.getUsername(), user.getPassword())) {
//            return "redirect:/"; // Redirect to hello page after successful login
//        } else {
//            model.addAttribute("loginError", "Invalid username or password.");
//            return "login";
//        }
//    }

    // Show registration form
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    // Process registration form
    @PostMapping("/register")
    public String processRegistration(@ModelAttribute("user") User user, BindingResult result, Model model) {
        // Validate the registration data
        if (result.hasErrors()) {
            return "register";
        }

        // Check if the user already exists
        if (userService.checkIfUserExists(user.getUsername())) {
            model.addAttribute("registrationError", "Username already exists.");
            return "register";
        }

        // Save the new user
        userService.addUser(user.getName(), user.getEmail(), user.getUsername(), user.getPassword());
        model.addAttribute("message", "Registration successful! Please log in.");
        return "redirect:/login"; // Redirect to login page after registration
    }

    // Show result page
    @GetMapping("/result")
    public String showResult(Model model) {
        model.addAttribute("user", new User());
        return "result";
    }

    // Process result
    @PostMapping("/result")
    public String processResult(@ModelAttribute("user") User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "result";
        }
        model.addAttribute("user", user);
        return "result";
    }
}
