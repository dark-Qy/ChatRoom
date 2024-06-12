package com.example.demo.service;

import com.example.demo.config.UserConfig;
import com.example.demo.exception.InvalidCredentialsException;
import com.example.demo.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserConfig userConfig;

    @Autowired
    public UserService(UserConfig userConfig) {
        this.userConfig = userConfig;
    }

    public User login(String username, String password) {
        List<User> users = userConfig.getUsers();
        if (users != null) {
            for (User user : users) {
                System.out.println("username: " + user.getUsername());
                if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                    return user;
                }
            }
        }
        throw new InvalidCredentialsException("Invalid username or password");
    }
}
