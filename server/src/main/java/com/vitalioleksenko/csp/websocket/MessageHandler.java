package com.vitalioleksenko.csp.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vitalioleksenko.csp.models.dto.NotificationMessage;
import com.vitalioleksenko.csp.services.notification.PendingNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MessageHandler extends TextWebSocketHandler {

    private final Map<Integer, List<WebSocketSession>> sessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;
    private final PendingNotificationService pendingNotificationService;

    @Autowired
    public MessageHandler(ObjectMapper objectMapper,
                          PendingNotificationService pendingNotificationService) {
        this.objectMapper = objectMapper;
        this.pendingNotificationService = pendingNotificationService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        Integer userId = (Integer) session.getAttributes().get("userId");

        if (userId == null) {
            try { session.close(); } catch (Exception ignored) {}
            return;
        }

        sessions.computeIfAbsent(userId, k -> new ArrayList<>()).add(session);

        pendingNotificationService.flushPendingForUser(userId, msg -> {
            try {
                sendToUser(userId, msg);
            } catch (IOException e) {
                pendingNotificationService.addPending(userId, msg);
            }
        });
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        Integer userId = (Integer) session.getAttributes().get("userId");
        sessions.getOrDefault(userId, List.of()).remove(session);
    }

    public void sendToUser(int userId, NotificationMessage notification) throws IOException {
        String json = objectMapper.writeValueAsString(notification);
        List<WebSocketSession> userSessions = sessions.get(userId);

        if (userSessions == null || userSessions.isEmpty()) {
            pendingNotificationService.addPending(userId, notification);
            return;
        }

        boolean delivered = false;

        for (WebSocketSession s : userSessions) {
            if (s.isOpen()) {
                s.sendMessage(new TextMessage(json));
                delivered = true;
            }
        }

        if (!delivered) {
            pendingNotificationService.addPending(userId, notification);
        }
    }
}

