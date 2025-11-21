package com.vitaliioleksenko.csp.client.model.resource;

import com.vitaliioleksenko.csp.client.util.enums.ResourceFormat;
import com.vitaliioleksenko.csp.client.util.enums.ResourceType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResourceCreateDTO {
    private Integer groupId;
    private Integer userId;
    private String title;
    private ResourceType type;
    private ResourceFormat format;
    private String pathOrUrl;
}
