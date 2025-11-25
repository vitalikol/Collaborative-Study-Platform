package com.vitalioleksenko.csp.models.dto.task;

import com.vitalioleksenko.csp.models.dto.group.GroupShortDTO;
import com.vitalioleksenko.csp.models.dto.user.UserShortDTO;
import com.vitalioleksenko.csp.models.enums.TaskStatus;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskPartialDTO {
    private int taskId;

    private GroupShortDTO group;

    private UserShortDTO user;

    @Size(min = 5, message = "Title must be at least 5 characters long")
    private String title;

    private TaskStatus status;

    private LocalDateTime deadline;
}
