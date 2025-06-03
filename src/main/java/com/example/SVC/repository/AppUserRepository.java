package com.example.SVC.repository;

import com.example.SVC.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    boolean existsByName(String name);
    Optional <AppUser> findByName(String username);
}