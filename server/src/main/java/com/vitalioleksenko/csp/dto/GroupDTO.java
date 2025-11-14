package com.vitalioleksenko.csp.dto;

import com.vitalioleksenko.csp.models.Membership;
import com.vitalioleksenko.csp.models.Resource;
import com.vitalioleksenko.csp.models.Task;
import com.vitalioleksenko.csp.models.User;
import jakarta.persistence.*;
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
public class GroupDTO {
    @Id
    @Column(name = "group_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int groupId;

    @Column(name = "name")
    @NotEmpty(message = "Name must not be empty")
    @Size(min = 5, message = "Name must be at least 5 characters long")
    private String name;

    @Column(name = "description")
    @NotEmpty(message = "Description must not be empty")
    @Size(min = 40, message = "Description must be at least 40 characters long")
    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
