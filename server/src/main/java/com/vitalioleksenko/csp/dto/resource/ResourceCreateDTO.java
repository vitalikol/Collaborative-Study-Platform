package com.vitalioleksenko.csp.dto.resource;

import com.vitalioleksenko.csp.util.enums.ResourceFormat;
import com.vitalioleksenko.csp.util.enums.ResourceType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResourceCreateDTO {
    private Integer taskId;

    private Integer userId;

    @NotEmpty(message = "Title must not be empty")
    @Size(min = 5, message = "Title must be at least 5 characters long")
    private String title;

    @NotNull(message = "Type must not be empty")
    private ResourceType type;

    @NotNull(message = "Format must not be empty")
    private ResourceFormat format;

    @NotEmpty(message = "Path or url must not be empty")
    private String pathOrUrl;
}
