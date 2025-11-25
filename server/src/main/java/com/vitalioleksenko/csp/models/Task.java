package com.vitalioleksenko.csp.models;

import com.vitalioleksenko.csp.models.enums.TaskStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Tasks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {
    @Id
    @Column(name = "task_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int taskId;

    @ManyToOne
    @JoinColumn(name = "group_id", referencedColumnName = "group_id")
    private Group group;

    @ManyToOne
    @JoinColumn(name = "created_by", referencedColumnName = "user_id")
    private User user;

    @OneToMany(mappedBy = "task")
    private List<Resource> resources;

    @Column(name = "title")
    @NotEmpty(message = "Title must not be empty")
    @Size(min = 5, message = "Title must be at least 5 characters long")
    private String title;

    @Column(name = "description")
    @NotEmpty(message = "Description must not be empty")
    @Size(min = 40, message = "Description must be at least 40 characters long")
    private String description;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @Column(name = "deadline",  columnDefinition = "TEXT")
    private LocalDateTime deadline;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
