package com.vitalioleksenko.csp.models.dto.resource;

import com.vitalioleksenko.csp.models.dto.task.TaskShortDTO;
import com.vitalioleksenko.csp.models.dto.user.UserShortDTO;
import com.vitalioleksenko.csp.models.enums.ResourceFormat;
import com.vitalioleksenko.csp.models.enums.ResourceType;
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

    private TaskShortDTO task;

    private UserShortDTO user;

    @NotEmpty(message = "Title must not be empty")
    @Size(min = 5, message = "Title must be at least 5 characters long")
    private String title;

    @NotEmpty(message = "Type must not be empty")
    private ResourceType type;

    @NotEmpty(message = "Format must not be empty")
    private ResourceFormat format;

    private String pathOrUrl;

    private LocalDateTime uploadedAt;
}
