package com.vitalioleksenko.csp.services;

import com.vitalioleksenko.csp.models.Task;
import com.vitalioleksenko.csp.models.dto.NotificationMessage;
import com.vitalioleksenko.csp.models.enums.NotificationType;
import com.vitalioleksenko.csp.repositories.MembershipsRepository;
import com.vitalioleksenko.csp.websocket.MessageHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class NotificationService {

    private final MessageHandler messageHandler;
    private final MembershipsRepository membershipRepository;

    public NotificationService(MessageHandler messageHandler,
                               MembershipsRepository membershipRepository) {
        this.messageHandler = messageHandler;
        this.membershipRepository = membershipRepository;
    }

    public void notifyTaskCreated(Task task) {
        List<Integer> userIds = membershipRepository.findUserIdsByGroupId(task.getGroup().getGroupId());

        NotificationMessage msgTemplate = new NotificationMessage(
                NotificationType.TASK_CREATED,
                "New task created",
                "New task '" + task.getTitle() + "' in group " + task.getGroup().getName(),
                (long) task.getTaskId(),
                (long) task.getGroup().getGroupId()
        );

        for (int userId : userIds) {
            try {
                messageHandler.sendToUser(userId, msgTemplate);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void notifyDeadlineSoon(Task task) {
        List<Integer> userIds = membershipRepository.findUserIdsByGroupId(task.getGroup().getGroupId());

        NotificationMessage msgTemplate = new NotificationMessage(
                NotificationType.DEADLINE_SOON,
                "Deadline approaching",
                "Task '" + task.getTitle() + "' deadline is near",
                (long) task.getTaskId(),
                (long) task.getGroup().getGroupId()
        );

        for (int userId : userIds) {
            try {
                messageHandler.sendToUser(userId, msgTemplate);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

