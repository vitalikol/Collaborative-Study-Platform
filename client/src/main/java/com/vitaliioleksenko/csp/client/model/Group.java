package com.vitaliioleksenko.csp.client.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Group {
    private int groupId;
    private String name;
    private String description;
    private int memberCount;
}
