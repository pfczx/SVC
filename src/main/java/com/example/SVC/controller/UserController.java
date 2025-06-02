package com.example.SVC.controller;

import com.example.SVC.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.SVC.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    // Konstruktor dla Dependency Injection
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestParam String username,
                                             @RequestParam String password) {
        try {
            userService.createUser(username, password);
            return ResponseEntity.ok("User created successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Something went wrong: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(@RequestParam String username,
                                             @RequestParam String password) {
        try {
            userService.deleteUser(username, password);
            return ResponseEntity.ok("User deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Something went wrong: " + e.getMessage());
        }
    }

    @GetMapping("/showAll")
    public List<User> showAllUsers() {
        return userService.getUsers();
    }

    @PatchMapping("/edit")
    public ResponseEntity<String> editUser(@RequestParam String username,
                                           @RequestParam String oldpassword,
                                           @RequestParam String newpassword) {
        try {
            userService.editUser(username, oldpassword, newpassword);
            return ResponseEntity.ok("User edited successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Something went wrong: " + e.getMessage());
        }
    }
}