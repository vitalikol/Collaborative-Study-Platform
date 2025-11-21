package com.vitaliioleksenko.csp.client.model.resource;

import com.vitaliioleksenko.csp.client.model.group.GroupShort;
import com.vitaliioleksenko.csp.client.model.user.UserShort;
import com.vitaliioleksenko.csp.client.util.enums.ResourceFormat;
import com.vitaliioleksenko.csp.client.util.enums.ResourceType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResourceDetailedDTO {
    private int resourceId;
    private GroupShort group;
    private UserShort user;
    private String title;
    private ResourceType type;
    private ResourceFormat format;
    private String pathOrUrl;

    private LocalDateTime uploadedAt;
}
