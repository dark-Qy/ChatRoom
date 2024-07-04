package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class OnlineUserManager {

    private final ConcurrentHashMap<String, String> onlineUsers = new ConcurrentHashMap<>();

    public void userLoggedIn(String username, String ipAddress) {
        // 记录用户登录成功，将用户标记为在线
        onlineUsers.put(username, ipAddress);
    }

    public void userLoggedOut(String username) {
        // 用户登出，从在线用户列表中移除
        onlineUsers.remove(username);
    }

    public void listOnlineUsers() {
        System.out.println("在线用户列表：");
        for (String username : onlineUsers.keySet()) {
            System.out.println("username:" + username + " (IP: " + onlineUsers.get(username) + ")");
        }
    }

    public List<String> getOnlineUsersList() {
        List<String> userList = new ArrayList<>();
        for (String username : onlineUsers.keySet()) {
            userList.add("username:" + username + " (IP: " + onlineUsers.get(username) + ")");
        }
        return userList;
    }

}
