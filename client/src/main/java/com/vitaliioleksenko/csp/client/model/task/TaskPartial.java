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
public class TaskPartial {
    private int taskId;
    private GroupShort group;
    private UserShort user;
    private String title;
    private TaskStatus status;
    private LocalDateTime deadline;
}
