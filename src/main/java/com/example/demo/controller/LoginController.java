package com.example.demo.controller;

import com.example.demo.entity.Customer;
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
    public String hello(Model model) {
        return "hello";
    }

    // Show login form
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/login/adduser")
    public String addUser(@ModelAttribute("user") User user, Model model, RedirectAttributes redirectAttributes) {
        if (userService.addUser(user.getName(), user.getEmail(), user.getUsername(), user.getPassword())) {
            return "redirect:/result";
        } else {
            model.addAttribute("loginError", "Invalid username or password.");
            return "redirect:/";
        }

    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("customer", new Customer());
        return "register";
    }

    @PostMapping("/register")
    public String processRegistration(@ModelAttribute("customer") Customer customer, BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            return "register";
        }

        if (userService.checkIfUserExists(customer.getName())) {
            model.addAttribute("registrationError", "Username already exists.");
            return "register";
        }

        userService.addUser(customer.getName(), customer.getEmail(), customer.getPhone(), customer.getPassword());
        model.addAttribute("message", "Registration successful! Please log in.");
        return "redirect:/login";
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
