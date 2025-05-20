package com.example.SVC.service;

import org.springframework.stereotype.Service;

import com.example.SVC.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import com.example.SVC.model.UserClass;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void createUser(String username, String password) {
        UserClass user = new UserClass();
        user.setName(username);
        user.setPassword(password);
        userRepository.save(user);
    }

    

}
