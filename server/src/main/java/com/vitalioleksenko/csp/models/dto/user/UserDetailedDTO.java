package com.vitalioleksenko.csp.models.dto.user;

import com.vitalioleksenko.csp.models.dto.group.GroupShortDTO;
import com.vitalioleksenko.csp.models.dto.resource.ResourceShortDTO;
import com.vitalioleksenko.csp.models.dto.task.TaskShortDTO;
import com.vitalioleksenko.csp.security.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDetailedDTO {
    private int userId;

    private List<GroupShortDTO> createdGroups;

    private List<TaskShortDTO> tasks;

    private List<ResourceShortDTO> resources;

    @NotEmpty(message = "Name must not be empty")
    @Size(min = 5, message = "Name must be at least 5 characters long")
    private String name;

    @NotEmpty(message = "Email must not be empty")
    @Email(message = "Email must be valid")
    //@UniqueValue(message = "Email must be unique", fieldName = "email", entityClass = User.class)
    private String email;

    private Role role;

}
