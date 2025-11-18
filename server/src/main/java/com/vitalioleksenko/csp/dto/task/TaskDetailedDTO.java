package com.vitalioleksenko.csp.dto.task;

import com.vitalioleksenko.csp.dto.group.GroupShortDTO;
import com.vitalioleksenko.csp.dto.user.UserShortDTO;
import com.vitalioleksenko.csp.models.Group;
import com.vitalioleksenko.csp.models.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskDetailedDTO {
    private int taskId;

    private GroupShortDTO group;

    private UserShortDTO user;

    @NotEmpty(message = "Title must not be empty")
    @Size(min = 5, message = "Title must be at least 5 characters long")
    private String title;

    @NotEmpty(message = "Description must not be empty")
    @Size(min = 40, message = "Description must be at least 40 characters long")
    private String description;

    @NotEmpty(message = "Status must not be empty")
    private String status;

    @NotEmpty(message = "Deadline must not be empty")
    private LocalDateTime deadline;

    private LocalDateTime createdAt;
}
