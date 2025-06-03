package com.example.SVC.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.SVC.repository.AppUserRepository;
import com.example.SVC.model.AppUser;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {

    @Autowired
    private final AppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional <AppUser> user = appUserRepository.findByName(username);
        if (user.isPresent()) {

            var userObj = user.get();
            return User.builder()
                    .username(userObj.getName())
                    .password(userObj.getPassword())
                    .build();

        }else{
            throw new UsernameNotFoundException(username);
        }
    }

//    public void createUser(String username, String password) {
//        AppUser user = new AppUser();
//        user.setName(username);
//        user.setPassword(password);
//        if(!appUserRepository.existsByName(username))
//            appUserRepository.save(user);
//        else
//            throw new IllegalArgumentException("Username already exists");
//    }
//
//    public void deleteUser(String username, String password) {
//        Optional<AppUser> userOptional = appUserRepository.findByName(username);
//
//        if (userOptional.isPresent()) {
//            AppUser user = userOptional.get();
//            if (user.getPassword().equals(password)) {
//                appUserRepository.delete(user);
//            } else {
//                throw new IllegalArgumentException("Incorrect password");
//            }
//        } else {
//            throw new IllegalArgumentException("Username does not exist");
//        }
//    }
//
//
//    public List<AppUser> getUsers() {
//        return appUserRepository.findAll();
//    }
//
//    public void editUser(String username, String oldPassword, String newPassword) {
//        Optional<AppUser> userOptional = appUserRepository.findByName(username);
//
//        if (userOptional.isPresent()) {
//            AppUser user = userOptional.get();
//            if (user.getPassword().equals(oldPassword)) {
//                user.setPassword(newPassword);
//                appUserRepository.save(user);
//            } else {
//                throw new IllegalArgumentException("Incorrect current password");
//            }
//        } else {
//            throw new IllegalArgumentException("User not found");
//        }
//    }


}