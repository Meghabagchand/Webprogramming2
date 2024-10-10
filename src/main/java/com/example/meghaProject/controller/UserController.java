package com.example.meghaProject.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.meghaProject.model.Business;
import com.example.meghaProject.model.Comment;
import com.example.meghaProject.model.User;
import com.example.meghaProject.model.User.Role;
import com.example.meghaProject.service.BusinessService;
import com.example.meghaProject.service.CommentService;
import com.example.meghaProject.service.UserService;

@Controller
public class UserController {

    @Autowired
    public UserService userService;

    @Autowired
    public BusinessService businessService;

    @Autowired
    private CommentService commentService;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @GetMapping("/")
    public String home(Model model) {
        logger.info("Index page");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = null;
        List<Business> businesses = (List<Business>) businessService.getAllBusiness();

        for (Business business : businesses) {
            List<Comment> comments = commentService.getCommentsByBusiness(business);
            business.setComments(comments);
        }
        boolean isAuthenticated = !(auth instanceof AnonymousAuthenticationToken) && auth.isAuthenticated();

        if (isAuthenticated) {
            currentUsername = auth.getName();
        }

        logger.info("Current User: " + currentUsername);

        model.addAttribute("isAuthenticated", isAuthenticated);
        model.addAttribute("hasAdminRole", auth.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN")));

        model.addAttribute("businesses", businessService.getAllBusiness());
        model.addAttribute("currentUsername", currentUsername);
        return "index";
    }

    @GetMapping("/login")
    public String getLoginPage(Model model) {
        logger.info("Login Page");
        return "login";
    }

    @GetMapping("/register")
    public String getRegistrationPage(Model model) {
        logger.info("Registration Page");
        return "register";
    }

    @PostMapping("/register")
    public String login(@ModelAttribute User user, Model model) {
        logger.info("Working");
        if (userService.checkIfUserExists(user.getUsername())) {
            model.addAttribute("registrationError", "Username already exists.");
            return "redirect:/dashboard";
        }

        if (user.getUsername().equals("admin")) {
            userService.addUser(user.getUsername(), user.getPassword(), Role.ROLE_ADMIN);
            model.addAttribute("message", "Registration successful! Please log in.");
            return "redirect:/dashboard";
        } else {
            userService.addUser(user.getUsername(), user.getPassword(), Role.ROLE_USER);
            model.addAttribute("message", "Registration successful! Please log in.");
            return "redirect:/";

        }
    }

}