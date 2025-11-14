package com.vitaliioleksenko.csp.client.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {
    private int taskId;
    private String title;
    private String description;
    private String status;
    private LocalDateTime deadline;
    private LocalDateTime createdAt;
}