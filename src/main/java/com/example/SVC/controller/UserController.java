package com.example.SVC.controller;

import com.example.SVC.model.UserClass;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import com.example.SVC.service.UserService;

import java.util.List;

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
