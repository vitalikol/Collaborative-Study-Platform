package com.vitaliioleksenko.csp.client.model.activity;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityLogDetailedDTO {
    private int logId;
    private String action;
    private LocalDateTime timestamp;
    private String details;
}
