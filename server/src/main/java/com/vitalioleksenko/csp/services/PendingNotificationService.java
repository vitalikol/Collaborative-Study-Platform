package com.vitalioleksenko.csp.services;

import com.vitalioleksenko.csp.models.dto.NotificationMessage;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class PendingNotificationService {
    private final Map<Integer, List<NotificationMessage>> pending = new ConcurrentHashMap<>();

    public void addPending(int userId, NotificationMessage msg) {
        pending.computeIfAbsent(userId, k -> new CopyOnWriteArrayList<>()).add(msg);
    }

    public void flushPendingForUser(int userId, java.util.function.Consumer<NotificationMessage> sender) {
        List<NotificationMessage> list = pending.remove(userId);
        if (list == null || list.isEmpty()) return;

        for (NotificationMessage msg : list) {
            sender.accept(msg);
        }
    }
}

