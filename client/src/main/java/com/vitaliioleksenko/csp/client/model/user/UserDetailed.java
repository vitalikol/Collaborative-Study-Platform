package com.vitaliioleksenko.csp.client.model.user;

import com.vitaliioleksenko.csp.client.model.activity.ActivityLogShortDTO;
import com.vitaliioleksenko.csp.client.model.group.GroupShort;
import com.vitaliioleksenko.csp.client.model.resource.ResourceShortDTO;
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
    private List<ResourceShortDTO> resources;
    private List<ActivityLogShortDTO> activities;
    private String name;
    private String email;
    private Role role;
}
