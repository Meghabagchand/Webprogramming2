package com.example.demo.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import entity.Data;

import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class Hello_Controller {




	@GetMapping("/")
    public String hello() {
        return "hello";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new Data()); // Corrected
        return "login";
    }

    @PostMapping("/login/adduser")
    public String addUser( Data user, Model model) { // Corrected
        
		if ("megha".equals(user.getName()) && "m123".equals(user.getPassword())) { // Corrected
            return "redirect:/add";
        } else {
            model.addAttribute("loginError", "Invalid username or password.");
            return "login";
        }
		
    }
    @GetMapping("/add")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new Data());  // Add empty user object to model
        return "add";  // Return the view name of your HTML (add.html)
    }
    @PostMapping("/add")
    public String processRegistration(Data user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "add";  // If validation fails, show the form again
        }
        model.addAttribute("message", "Registration successful!");
        return "success";  // Redirect to a success page after successful registration
    }
    @GetMapping("/result")
    public String showResult(Model model) {
        model.addAttribute("user", new Data());  // Add empty User object to the model
        return "result";  // Return the view name (register.html)
    }
    @PostMapping("/result")
    public String processResult(Data user, BindingResult result, Model model) {
        // If there are validation errors, return the form
        if (result.hasErrors()) {
            return "result";
        }

        // Pass the user data to the model to display it on the result page
        model.addAttribute("user", user);

        // Return the result view (result.html) to display the submitted data
        return "result";
    }
}
