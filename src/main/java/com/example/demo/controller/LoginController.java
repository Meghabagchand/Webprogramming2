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
    public String addUser(@ModelAttribute("user") User user, Model model) {
        if (userService.addUser(user.getUsername(), user.getPassword())) {
            return "redirect:/add";
        } else {
            model.addAttribute("loginError", "Invalid username or password.");
            return "login";
        }
    }

//    @GetMapping("/add")
//    public String showRegistrationForm(Model model) {
//        model.addAttribute("user", new Data());  
//        return "add";
//    }
//
//    @PostMapping("/add")
//    public String processRegistration(@ModelAttribute("user") Data user, BindingResult result, Model model) {
//        if (result.hasErrors()) {
//            return "add";
//        }
//        model.addAttribute("message", "Registration successful!");
//        return "success";
//    }

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

    @GetMapping("/register")
    public String registerPage() {
        return "register"; 
    }

    @PostMapping("/register")
    public String resultData(@ModelAttribute User user, Model model) {
        return "result";
    }
}