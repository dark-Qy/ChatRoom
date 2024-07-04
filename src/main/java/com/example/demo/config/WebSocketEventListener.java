package com.example.demo.config;

import com.example.demo.service.OnlineUserManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

@Component
public class WebSocketEventListener {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final Set<String> activeSubscriptions = new HashSet<>();
    private final OnlineUserManager onlineUserManager;

    public WebSocketEventListener(OnlineUserManager onlineUserManager) {
        this.onlineUserManager = onlineUserManager;
    }

    @EventListener
    public void handleSessionSubscribeEvent(SessionSubscribeEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        if (StompCommand.SUBSCRIBE.equals(headerAccessor.getCommand())) {
            String sessionId = headerAccessor.getSessionId();
            String destination = headerAccessor.getDestination();
            logger.info("User with session id {} subscribed to {}", sessionId, destination);

            // Add the subscription to the set of active subscriptions
            activeSubscriptions.add(destination);
        }
    }
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        String username = (String) headerAccessor.getSessionAttributes().get("username"); // Assuming username is stored in session attributes
        String disconnectTime = LocalDateTime.now().format(dateTimeFormatter);
        // Record disconnect time with username
        logger.info("User {} disconnected. SessionId: {}, Disconnect Time: {}", username, sessionId, disconnectTime);
    }

    public boolean isUserSubscribed(String destination) {
        return activeSubscriptions.contains(destination);
    }
}
