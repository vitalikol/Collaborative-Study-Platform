package com.vitaliioleksenko.csp.client.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationMessage {
    private String type;
    private String title;
    private String message;
    private Long taskId;
    private Long groupId;
    private long timestamp;

}
