package com.example.SVC.controller;

import com.example.SVC.model.AppUser;
import com.example.SVC.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.SVC.service.AppUserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class AppUserController {

    private final AppUserService appUserService;

    @Autowired
    private AppUserRepository userRepository;

    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @PostMapping(value = "/register", consumes = "application/json")
    public AppUser registerUser(@RequestBody AppUser appUser) {
        return userRepository.save(appUser);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(@RequestParam String username,
                                             @RequestParam String password) {
        try {
            appUserService.deleteUser(username, password);
            return ResponseEntity.ok("User deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Something went wrong: " + e.getMessage());
        }
    }

    @GetMapping("/showAll")
    public List<AppUser> showAllUsers() {
        return appUserService.getUsers();
    }

    @PatchMapping("/edit")
    public ResponseEntity<String> editUser(@RequestParam String username,
                                           @RequestParam String oldpassword,
                                           @RequestParam String newpassword) {
        try {
            appUserService.editUser(username, oldpassword, newpassword);
            return ResponseEntity.ok("User edited successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Something went wrong: " + e.getMessage());
        }
    }
}