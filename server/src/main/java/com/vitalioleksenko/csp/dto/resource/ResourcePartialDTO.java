package com.vitalioleksenko.csp.dto.resource;

import com.vitalioleksenko.csp.dto.group.GroupShortDTO;
import com.vitalioleksenko.csp.dto.user.UserShortDTO;
import com.vitalioleksenko.csp.models.Group;
import com.vitalioleksenko.csp.models.User;
import com.vitalioleksenko.csp.util.ResourceFormat;
import com.vitalioleksenko.csp.util.ResourceType;
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
public class ResourcePartialDTO {
    private int resourceId;

    private GroupShortDTO group;

    private UserShortDTO user;

    @NotEmpty(message = "Title must not be empty")
    @Size(min = 5, message = "Title must be at least 5 characters long")
    private String title;

    @NotEmpty(message = "Type must not be empty")
    private ResourceType type;

    @NotEmpty(message = "Format must not be empty")
    private ResourceFormat format;

    private LocalDateTime uploadedAt;
}
