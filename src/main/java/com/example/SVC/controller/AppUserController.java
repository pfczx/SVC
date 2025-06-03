package com.example.SVC.controller;

import com.example.SVC.model.AppUser;
import com.example.SVC.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.SVC.service.AppUserService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@RestController
public class AppUserController {

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostMapping(value = "/req/register", consumes = "application/json")
    public AppUser createUser(@RequestBody AppUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);

    }
}