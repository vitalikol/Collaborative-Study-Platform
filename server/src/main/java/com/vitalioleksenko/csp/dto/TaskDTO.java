package com.vitalioleksenko.csp.dto;

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
public class TaskDTO {
    @Id
    @Column(name = "task_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int taskId;

    @Column(name = "title")
    @NotEmpty(message = "Title must not be empty")
    @Size(min = 5, message = "Title must be at least 5 characters long")
    private String title;

    @Column(name = "description")
    @NotEmpty(message = "Description must not be empty")
    @Size(min = 40, message = "Description must be at least 40 characters long")
    private String description;

    @Column(name = "status")
    @NotEmpty(message = "Status must not be empty")
    private String status;

    @Column(name = "deadline")
    @NotEmpty(message = "Deadline must not be empty")
    private LocalDateTime deadline;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
