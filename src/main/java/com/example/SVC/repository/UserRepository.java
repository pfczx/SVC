package com.example.SVC.repository;

import com.example.SVC.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByName(String name);
    User findByName(String username);
}