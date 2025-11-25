package com.vitalioleksenko.csp.models.dto.group;

import com.vitalioleksenko.csp.models.dto.user.UserShortDTO;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupPartialDTO {
    private int groupId;

    @NotEmpty(message = "Name must not be empty")
    @Size(min = 5, message = "Name must be at least 5 characters long")
    private String name;

    @NotEmpty(message = "Description must not be empty")
    @Size(min = 40, message = "Description must be at least 40 characters long")
    private String description;

    private UserShortDTO createdBy;

    private LocalDateTime createdAt;
}
