package com.vitaliioleksenko.csp.client.model.task;

import com.vitaliioleksenko.csp.client.model.group.GroupShort;
import com.vitaliioleksenko.csp.client.model.user.UserShort;
import com.vitaliioleksenko.csp.client.util.enums.TaskStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskDetailed {
    private int taskId;
    private GroupShort group;
    private UserShort user;
    private String title;
    private String description;
    private TaskStatus status;
    private LocalDateTime deadline;
    private LocalDateTime createdAt;
}
