package com.vitalioleksenko.csp.dto.group;

import com.vitalioleksenko.csp.dto.membership.MembershipShortDTO;
import com.vitalioleksenko.csp.dto.resource.ResourceShortDTO;
import com.vitalioleksenko.csp.dto.task.TaskShortDTO;
import com.vitalioleksenko.csp.dto.user.UserShortDTO;
import com.vitalioleksenko.csp.models.Membership;
import com.vitalioleksenko.csp.models.Resource;
import com.vitalioleksenko.csp.models.Task;
import com.vitalioleksenko.csp.models.User;
import com.vitalioleksenko.csp.repositories.TasksRepository;
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
public class GroupDetailedDTO {
    private int groupId;

    private List<MembershipShortDTO> members;

    private List<TaskShortDTO> tasks;

    private List<ResourceShortDTO> resources;

    @NotEmpty(message = "Name must not be empty")
    @Size(min = 5, message = "Name must be at least 5 characters long")
    private String name;

    @NotEmpty(message = "Description must not be empty")
    @Size(min = 40, message = "Description must be at least 40 characters long")
    private String description;

    private UserShortDTO createdBy;

    private LocalDateTime createdAt;
}
