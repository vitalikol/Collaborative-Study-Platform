package com.vitaliioleksenko.csp.client.model.task;

import com.vitaliioleksenko.csp.client.util.enums.TaskStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskUpdate {
    private String title;
    private String description;
    private TaskStatus status;
    private LocalDateTime deadline;
}
