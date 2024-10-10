package com.example.meghaProject.service;

import com.example.meghaProject.model.*;
import com.example.meghaProject.repo.CommentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    public List<Comment> getCommentsByBusiness(Business business) {
        return commentRepository.findByBusiness(business);
    }

    public List<Comment> getCommentsByUser(User user) {
        return commentRepository.findByUser(user);
    }

    public Comment addComment(User user, Business business, String content) {
        Comment comment = new Comment();
        comment.setUser(user);
        comment.setBusiness(business);
        comment.setCommentText(content);
        comment.setCreatedAt(LocalDateTime.now());
        return commentRepository.save(comment);
    }

    public Optional<Comment> getCommentById(Long commentId) {
        return commentRepository.findById(commentId);
    }

    public void deleteComment(Comment comment) {
        commentRepository.delete(comment);
    }

    public Comment updateComment(Comment comment, String newContent) {
        comment.setCommentText(newContent);
        comment.setUpdatedAt(LocalDateTime.now());
        return commentRepository.save(comment);
    }
}
