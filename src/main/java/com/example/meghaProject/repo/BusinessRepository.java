package com.example.meghaProject.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.meghaProject.model.Business;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {

}
