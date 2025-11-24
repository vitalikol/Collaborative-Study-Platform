package com.vitaliioleksenko.csp.client.model.user;

import com.vitaliioleksenko.csp.client.model.activity.ActivityLogShort;
import com.vitaliioleksenko.csp.client.model.group.GroupShort;
import com.vitaliioleksenko.csp.client.model.resource.ResourceShort;
import com.vitaliioleksenko.csp.client.model.task.TaskShort;
import com.vitaliioleksenko.csp.client.util.enums.Role;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDetailed {
    private int userId;
    private List<GroupShort> createdGroups;
    private List<TaskShort> tasks;
    private List<ResourceShort> resources;
    private List<ActivityLogShort> activities;
    private String name;
    private String email;
    private Role role;
}
