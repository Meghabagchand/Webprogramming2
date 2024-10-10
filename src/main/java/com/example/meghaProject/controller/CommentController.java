package com.example.meghaProject.controller;

import com.example.meghaProject.model.*;
import com.example.meghaProject.repo.UserRepository;
import com.example.meghaProject.service.CommentService;
import com.example.meghaProject.service.BusinessService;
import com.example.meghaProject.service.UserService;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/business/{businessId}/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;
    @Autowired
    private BusinessService businessService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    // private static final Logger logger =
    // LoggerFactory.getLogger(UserService.class);

    @GetMapping("/add")
    public String addCommentForm(@PathVariable Long businessId, Model model) {
        Business business = businessService.getBusinessById(businessId);
        model.addAttribute("business", business);
        return "comments/add";
    }

    // @GetMapping("/list")
    // public String showComments(@PathVariable Long businessId, Model model) {
    // Business business = businessService.getBusinessById(businessId);
    // List<Comment> comments = commentService.getCommentsByBusiness(business);
    // model.addAttribute("comments", comments);
    // model.addAttribute("business", business);
    // return "comments/list";
    // }

    @PostMapping
    public String addComment(@PathVariable Long businessId,
            @RequestBody String content,
            Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        User user = userService.findByUsername(username);

        Business business = businessService.getBusinessById(businessId);
        commentService.addComment(user, business, content.split("\\=")[1].replaceAll("\\+", " "));
        return "redirect:/";
    }

    @GetMapping("/{commentId}/delete")
    public String deleteComment(@PathVariable Long businessId, @PathVariable Long commentId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String currentUsername = authentication.getName();
            User currentUser = userRepository.findByUsername(currentUsername);

            Comment comment = commentService.getCommentById(commentId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid comment ID"));
            if (comment.getUser().equals(currentUser)) {
                commentService.deleteComment(comment);
            }
        }
        return "redirect:/";
    }

    @PostMapping("/{commentId}/edit")
    public String editComment(@PathVariable Long businessId,
            @PathVariable Long commentId,
            @RequestParam String newContent,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Comment comment = commentService.getCommentById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid comment ID"));
        if (comment.getUser().equals(user)) {
            commentService.updateComment(comment, newContent);
        }
        return "redirect:/businesses/" + businessId + "/comments";
    }
}
