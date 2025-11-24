package com.vitaliioleksenko.csp.client.model.resource;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResourceShort {
    private int resourceId;
    private String title;
}
