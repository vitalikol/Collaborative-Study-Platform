package com.vitaliioleksenko.csp.client.model;

import com.vitaliioleksenko.csp.client.util.ResourceType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class Resource {
    private int resourceId;
    private String title;
    private ResourceType type;
    private String pathOrUrl;
    private LocalDateTime uploadedAt;
    private User uploadedBy;
}
