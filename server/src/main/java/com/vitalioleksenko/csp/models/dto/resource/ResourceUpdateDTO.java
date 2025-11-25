package com.vitalioleksenko.csp.models.dto.resource;

import com.vitalioleksenko.csp.models.enums.ResourceFormat;
import com.vitalioleksenko.csp.models.enums.ResourceType;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResourceUpdateDTO {
    @Size(min = 5, message = "Title must be at least 5 characters long")
    private String title;

    private ResourceType type;

    private ResourceFormat format;

    private String pathOrUrl;
}
