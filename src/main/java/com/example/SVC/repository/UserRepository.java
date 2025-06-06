package com.example.SVC.repository;

import com.example.SVC.model.UserClass;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserClass, Long> {
    boolean existsByName(String name);
    UserClass findByName(String username);

}