package com.vitalioleksenko.csp.models.dto.task;

import com.vitalioleksenko.csp.models.enums.TaskStatus;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskUpdateDTO {
    @Size(min = 5, message = "Title must be at least 5 characters long")
    private String title;

    @Size(min = 40, message = "Description must be at least 40 characters long")
    private String description;

    private TaskStatus status;

    private LocalDateTime deadline;
}
