package com.example.demo.controller;

import com.example.demo.models.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/send")
    public void sendMessage(ChatMessage chatMessage) {
        if (chatMessage.getText().startsWith("@@")) {
            // Handle system command
            chatMessage.setType(ChatMessage.MessageType.COMMAND);
            messagingTemplate.convertAndSend("/topic/public", chatMessage);
        } else if (chatMessage.getText().startsWith("@")) {
            // Handle private message
            chatMessage.setType(ChatMessage.MessageType.PRIVATE);
            String toUser = chatMessage.getText().split(" ")[0].substring(1);
            chatMessage.setTo(toUser);
            messagingTemplate.convertAndSendToUser(toUser, "/queue/private", chatMessage);
        } else {
            // Handle public chat message
            chatMessage.setType(ChatMessage.MessageType.CHAT);
            messagingTemplate.convertAndSend("/topic/public", chatMessage);
        }
    }
}
