package com.example.SVC.service;

import org.springframework.stereotype.Service;

import com.example.SVC.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import com.example.SVC.model.UserClass;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void createUser(String username, String password) {
        UserClass user = new UserClass();
        user.setName(username);
        user.setPassword(password);
        if(!userRepository.existsByName(username))
            userRepository.save(user);
        else
            throw new IllegalArgumentException("Username already exists");
    }

    public void deleteUser(String username, String password) {
        UserClass user = userRepository.findByName(username);

        if(user != null && user.getPassword().equals(password))
            userRepository.delete(user);
        else
            throw new IllegalArgumentException("Username does not exist");
    }

    public List<UserClass> getUsers() {
        return userRepository.findAll();
    }

    
}
