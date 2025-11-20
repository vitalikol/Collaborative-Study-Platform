package com.vitalioleksenko.csp.dto.task;

import com.vitalioleksenko.csp.util.enums.TaskStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskCreateDTO {
    @NotEmpty(message = "Title must not be empty")
    @Size(min = 5, message = "Title must be at least 5 characters long")
    private String title;

    @NotNull(message = "Group id must be provided")
    private Integer groupId;

    @NotNull(message = "User id must be provided")
    private Integer userId;

    @NotEmpty(message = "Description must not be empty")
    @Size(min = 40, message = "Description must be at least 40 characters long")
    private String description;

    private TaskStatus status;

    private LocalDateTime deadline;
}
