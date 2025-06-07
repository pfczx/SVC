package com.example.SVC.controller;

import com.example.SVC.model.UserClass;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import com.example.SVC.service.UserService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestParam String username, @RequestParam String password) {
        try {
            userService.createUser(username, password);
            return ResponseEntity.ok("User created successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(999).body("something went wrong");
        }
    }
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Map<String, String> body) {
        String username = body.get("name");
        String password = body.get("password");

        try {
            userService.createUser(username, password);
            return ResponseEntity.ok("User created");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(String username, String password) {
        try {
            userService.deleteUser(username, password);
            return ResponseEntity.ok("User deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(999).body("something went wrong");
        }
    }
    @GetMapping("/showAll")
    public List<UserClass> showAllUsers() {
        return userService.getUsers();
    }

    @PatchMapping("/edit")
    public ResponseEntity<String> editUser(String username, String oldpassword, String newpassword) {
        try {
            userService.editUser(username, oldpassword, newpassword);
            return ResponseEntity.ok("User edited successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(999).body("something went wrong");
        }
    }
}
