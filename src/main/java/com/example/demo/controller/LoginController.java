package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class LoginController {
    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String hello() {
        return "hello";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/login/adduser")
    public String addUser(@ModelAttribute("user") User user, Model model, RedirectAttributes redirectAttributes) {
        if (userService.addUser(user.getUsername(), user.getRole(), user.getPassword())) {
            return "redirect:/add";
        } else {
            model.addAttribute("loginError", "Invalid username or password.");
            redirectAttributes.addFlashAttribute("errorMessage", "Username cannot be empty.");
            return "login";
        }
    }

    @GetMapping("/add")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user");
        return "add";
    }

    // @PostMapping("/add")
    // public String processRegistration(@ModelAttribute("user") Data user,
    // BindingResult result, Model model) {
    // if (result.hasErrors()) {
    // return "add";
    // }
    // model.addAttribute("message", "Registration successful!");
    // return "success";
    // }

    @GetMapping("/result")
    public String showResult(Model model) {
        model.addAttribute("user", new User());
        return "result";
    }

    @PostMapping("/result")
    public String processResult(@ModelAttribute("user") User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "result";
        }
        model.addAttribute("user", user);
        return "result";
    }

    // @GetMapping("/register")
    // public String registerPage() {
    // return "register";
    // }

    // @PostMapping("/register")
    // public String resultData(@ModelAttribute User user, Model model) {
    // boolean isRegistered = userService.registerUser(user.getUsername(),
    // user.getPassword(),
    // user.getRole());

    // if (isRegistered) {
    // return "result";
    // } else {
    // model.addAttribute("errorMessage", "Username already exists.");
    // return "register";
    // }
    // }
}