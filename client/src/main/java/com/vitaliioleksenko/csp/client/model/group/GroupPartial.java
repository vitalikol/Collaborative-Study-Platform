package com.vitaliioleksenko.csp.client.model.group;


import com.vitaliioleksenko.csp.client.model.user.UserShort;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupPartial {
    private int groupId;
    private String name;
    private String description;
    private UserShort createdBy;
    private LocalDateTime createdAt;
}
