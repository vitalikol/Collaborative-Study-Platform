package com.vitalioleksenko.csp.dto.resource;

import com.vitalioleksenko.csp.dto.group.GroupShortDTO;
import com.vitalioleksenko.csp.dto.task.TaskShortDTO;
import com.vitalioleksenko.csp.dto.user.UserShortDTO;
import com.vitalioleksenko.csp.util.enums.ResourceFormat;
import com.vitalioleksenko.csp.util.enums.ResourceType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResourceDetailedDTO {
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

    @NotEmpty(message = "Path or url must not be empty")
    private String pathOrUrl;

    private LocalDateTime uploadedAt;
}
