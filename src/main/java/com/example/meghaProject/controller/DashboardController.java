package com.example.meghaProject.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.meghaProject.model.Comment;
import com.example.meghaProject.model.User;
import com.example.meghaProject.model.User.Role;
import com.example.meghaProject.service.*;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private UserService userService;

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private BusinessService businessService;

    @Autowired
    private CommentService commentService;

    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);

    @GetMapping
    public String getDashboard(Model model) {
        model.addAttribute("username", userService.getUser());
        model.addAttribute("users", userService.getAllUsers());
        return "dashboard";
    }

    @GetMapping("/users")
    public String getUsers(Model model) {
        model.addAttribute("username", userService.getUser());
        model.addAttribute("users", userService.getAllUsers());
        return "admin/users";
    }

    @GetMapping("/users/create")
    public String createUser(Model model) {
        logger.info("User creation page");
        model.addAttribute("username", userService.getUser());
        return "admin/user/createUser";
    }

    @PostMapping("/users/create")
    public String createUserPost(Model model, @ModelAttribute("user") User user) {
        logger.info("Creating user with username: " + user.getUsername());

        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            model.addAttribute("error", "Username cannot be empty.");
            return "userCreationForm";
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            model.addAttribute("error", "Password cannot be empty.");
            return "userCreationForm";
        }

        dashboardService.createUser(user.getUsername(), user.getPassword());
        return "redirect:/dashboard/users";
    }

    @GetMapping("/users/edit/{id}")
    public String editUser(Model model, @PathVariable Long id) {
        logger.info("Editing user with id: " + id);
        model.addAttribute("username", userService.getUser());
        model.addAttribute("user", userService.getUserById(id));
        return "admin/user/editUser";
    }

    @PostMapping("/users/edit/{id}")
    public String editBusiness(@PathVariable Long id, @RequestParam String name, Role role, Model model) {
        User user = (User) userService.getUserById(id);
        if (user == null) {
            return "redirect:/error";
        }
        user.setUsername(name);
        user.setRole(role);
        userService.saveUser(user);
        model.addAttribute("business", userService.getAllUsers());

        return "redirect:/dashboard/users";
    }

    @GetMapping("/users/delete/{id}")
    public String deleteBusiness(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/dashboard/users";
    }

    @GetMapping("/comments")
    public String showComments(Model model) {
        // Business business = businessService.getBusinessById(businessId);
        List<Comment> comments = commentService.getAllComments();

        model.addAttribute("username", userService.getUser());
        model.addAttribute("comments", comments);
        // model.addAttribute("business", business);
        return "comments/list";
    }

}