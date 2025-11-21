package com.vitaliioleksenko.csp.client.model.group;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupCreate {
    private String name;
    private String description;
    private Integer createdBy;
}
