package com.vitaliioleksenko.csp.client.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Group {
    private int groupId;
    private String name;
    private String description;
    private LocalDateTime createdAt;
}
