package com.example.SVC.service;

import org.springframework.stereotype.Service;

import com.example.SVC.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import com.example.SVC.model.UserClass;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void createUser(String username, String password) {
        if (!userRepository.existsByName(username)) {
            UserClass user = new UserClass();
            user.setName(username);
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("Username already exists");
        }
    }

    public void deleteUser(String username, String password) {
        Optional<UserClass> userOptional = userRepository.findByName(username);

        if (userOptional.isPresent()) {
            UserClass user = userOptional.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                userRepository.delete(user);
            } else {
                throw new IllegalArgumentException("Password is incorrect");
            }
        } else {
            throw new IllegalArgumentException("Username does not exist");
        }
    }

    public List<UserClass> getUsers() {
        return userRepository.findAll();
    }

    public void editUser(String username, String oldPassword, String newPassword) {
        Optional<UserClass> userOptional = userRepository.findByName(username);

        if (userOptional.isPresent()) {
            UserClass user = userOptional.get();
            if (passwordEncoder.matches(oldPassword, user.getPassword())) {
                user.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(user);
            } else {
                throw new IllegalArgumentException("Old password is incorrect");
            }
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }
}

