package com.example.demo.controller;
import com.example.demo.service.OnlineUserManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.demo.models.User;
import com.example.demo.service.UserService;
import com.example.demo.exception.InvalidCredentialsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private OnlineUserManager onlineUserManager;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            User loggedInUser = userService.login(user.getUsername(), user.getPassword());
            // 记录用户登录成功，并更新在线用户列表
            onlineUserManager.userLoggedIn(loggedInUser.getUsername(), "127.0.0.1");
            // 记录登录成功的信息
            logger.info("登录成功: 用户名={}, IP地址={}, 时间={}", loggedInUser.getUsername(), "127.0.0.1", new java.util.Date());
            return ResponseEntity.ok(new LoginResponse(loggedInUser.getUsername(), 200));
        } catch (InvalidCredentialsException e) {
            // 记录登录失败的信息
            logger.warn("登录失败: 用户名={}, IP地址={}, 时间={}, 原因={}", user.getUsername(), "127.0.0.1", new java.util.Date(), e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponse(e.getMessage(), 401));
        }
    }

    static class LoginResponse {
        private String message;
        private int status;

        public LoginResponse(String message, int status) {
            this.message = message;
            this.status = status;
        }

        // Getters and Setters
        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
