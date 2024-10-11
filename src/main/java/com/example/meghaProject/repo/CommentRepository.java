package com.example.meghaProject.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.meghaProject.model.Business;
import com.example.meghaProject.model.Comment;
import com.example.meghaProject.model.User;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByBusiness(Business business);

    List<Comment> findByUser(User user);
}
