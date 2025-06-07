package com.example.SVC.repository;

import com.example.SVC.model.UserClass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserClass, Long> {
    boolean existsByName(String name);
    Optional<UserClass> findByName(String username);
    List<UserClass> findAll();
}