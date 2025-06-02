package com.example.SVC.service;

import org.springframework.stereotype.Service;

import com.example.SVC.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import com.example.SVC.model.User;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void createUser(String username, String password) {
        User user = new User();
        user.setName(username);
        user.setPassword(password);
        if(!userRepository.existsByName(username))
            userRepository.save(user);
        else
            throw new IllegalArgumentException("Username already exists");
    }

    public void deleteUser(String username, String password) {
        User user = userRepository.findByName(username);

        if(user != null && user.getPassword().equals(password))
            userRepository.delete(user);
        else
            throw new IllegalArgumentException("Username does not exist");
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public void editUser(String username, String oldpassword, String newpassword) {
        User user = userRepository.findByName(username);
        if (user != null && user.getPassword().equals(oldpassword)) {
            user.setPassword(newpassword);
        }
        else
            throw new IllegalArgumentException("Something went wrong");

    }
}