package com.vitaliioleksenko.csp.client.model.group;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupUpdate {
    private String name;
    private String description;
}
