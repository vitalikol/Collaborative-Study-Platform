package com.vitaliioleksenko.csp.client.model.group;

import com.vitaliioleksenko.csp.client.model.membership.MembershipShort;
import com.vitaliioleksenko.csp.client.model.resource.ResourceShort;
import com.vitaliioleksenko.csp.client.model.task.TaskShort;
import com.vitaliioleksenko.csp.client.model.user.UserShort;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupDetailed {
    private int groupId;
    private List<MembershipShort> members;
    private List<TaskShort> tasks;
    private List<ResourceShort> resources;
    private String name;
    private String description;
    private UserShort createdBy;
    private LocalDateTime createdAt;
}
