package com.example.SVC.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import com.example.SVC.service.UserService;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<String> createUser(String username, String password) {
        try {
            userService.createUser(username, password);
            return ResponseEntity.ok("User created successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(999).body("something went wrong");
        }
    }
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(String username, String password) {
        try {
            userService.deleteUser(username, password);
            return ResponseEntity.ok("User created successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(999).body("something went wrong");
        }
    }
	
}
