package com.vitalioleksenko.csp.models.dto.task;

import com.vitalioleksenko.csp.models.dto.group.GroupShortDTO;
import com.vitalioleksenko.csp.models.dto.resource.ResourceShortDTO;
import com.vitalioleksenko.csp.models.dto.user.UserShortDTO;
import com.vitalioleksenko.csp.models.enums.TaskStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskDetailedDTO {
    private int taskId;

    private GroupShortDTO group;

    private UserShortDTO user;

    private List<ResourceShortDTO> resources;

    @NotEmpty(message = "Title must not be empty")
    @Size(min = 5, message = "Title must be at least 5 characters long")
    private String title;

    @NotEmpty(message = "Description must not be empty")
    @Size(min = 40, message = "Description must be at least 40 characters long")
    private String description;

    private TaskStatus status;

    private LocalDateTime deadline;

    private LocalDateTime createdAt;
}
