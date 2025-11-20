package com.vitalioleksenko.csp.dto.resource;

import com.vitalioleksenko.csp.util.enums.ResourceFormat;
import com.vitalioleksenko.csp.util.enums.ResourceType;
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
