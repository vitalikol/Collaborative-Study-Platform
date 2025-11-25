package com.vitalioleksenko.csp.models.dto;

import com.vitalioleksenko.csp.models.enums.NotificationType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationMessage {

    private NotificationType type;
    private String title;
    private String message;
    private Long taskId;
    private Long groupId;
    private long timestamp;


    public NotificationMessage(NotificationType type,
                               String title,
                               String message,
                               Long taskId,
                               Long groupId) {
        this.type = type;
        this.title = title;
        this.message = message;
        this.taskId = taskId;
        this.groupId = groupId;
        this.timestamp = System.currentTimeMillis();
    }
}

