package com.vitaliioleksenko.csp.client.model.resource;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResourceShortDTO {
    private int resourceId;
    private String title;
}
