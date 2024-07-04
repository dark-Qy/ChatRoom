package com.example.demo.controller;

import com.example.demo.models.ChatMessage;
import com.example.demo.service.OnlineUserManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class ChatController {

    private final OnlineUserManager onlineUserManager;
    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);
    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(SimpMessagingTemplate messagingTemplate, OnlineUserManager onlineUserManager) {
        this.messagingTemplate = messagingTemplate;
        this.onlineUserManager = onlineUserManager;
    }

    @MessageMapping("/send")
    public void sendMessage(ChatMessage chatMessage) {
        if (chatMessage.getText().startsWith("@@")) {
            // Handle system command
            chatMessage.setType(ChatMessage.MessageType.COMMAND);

            String command = chatMessage.getText().substring(2); // Extract the command without @@

            switch (command) {
                case "list":
                    // Implement logic to list current online users
                    List<String> onlineUsers = onlineUserManager.getOnlineUsersList();
                    chatMessage.setText("Online Users: " + onlineUsers);
                    break;
                case "quit":
                    // Inform the user to logout
                    chatMessage.setText("@@quit");
                    break;
                default:
                    // Handle unrecognized command
                    chatMessage.setText("System: Unrecognized command.");
                    break;
            }

            // Send response to the user
            messagingTemplate.convertAndSend("/topic/public", chatMessage);
            logger.debug("System command processed: {}", chatMessage);
        } else {
            // Handle public chat message
            chatMessage.setType(ChatMessage.MessageType.CHAT);
            messagingTemplate.convertAndSend("/topic/public", chatMessage);
            logger.debug("Public chat message sent: {}", chatMessage);
        }
    }
}
