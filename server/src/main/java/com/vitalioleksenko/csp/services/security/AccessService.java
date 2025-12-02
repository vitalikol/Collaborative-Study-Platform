package com.vitalioleksenko.csp.services.security;

import com.vitalioleksenko.csp.models.enums.GroupRole;
import com.vitalioleksenko.csp.repositories.MembershipsRepository;
import com.vitalioleksenko.csp.repositories.ResourcesRepository;
import com.vitalioleksenko.csp.repositories.TasksRepository;
import com.vitalioleksenko.csp.security.CustomUserDetails;
import com.vitalioleksenko.csp.util.exceptions.NotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service("accessService")
public class AccessService {
    private final TasksRepository tasksRepository;
    private final ResourcesRepository resourcesRepository;
    private final MembershipsRepository membershipsRepository;

    public AccessService(TasksRepository tasksRepository, ResourcesRepository resourcesRepository, MembershipsRepository membershipsRepository) {
        this.tasksRepository = tasksRepository;
        this.resourcesRepository = resourcesRepository;
        this.membershipsRepository = membershipsRepository;
    }

    public boolean isMemberOfGroup(Integer groupId) {
        if (groupId == null) {
            return false;
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof CustomUserDetails user))
            return false;

        int userId = user.getId();

        return membershipsRepository.existsByGroup_GroupIdAndUser_UserId(groupId, userId);
    }

    public boolean hasGroupRole(Integer groupId, GroupRole role){
        if (groupId == null)
            return false;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();

        int userId = user.getId();

        return membershipsRepository.existsByGroup_GroupIdAndUser_UserIdAndRole(groupId, userId, role);
    }

    public boolean isTeamLead(Integer groupId){
        return hasGroupRole(groupId, GroupRole.TEAM_LEAD);
    }

    public boolean isMemberOfGroupByTaskId(Integer taskId) {
        if (taskId == null)
            return true;
        int groupId = tasksRepository.findById(taskId).orElseThrow(NotFoundException::new).getGroup().getGroupId();

        return isMemberOfGroup(groupId);
    }

    public boolean isMemberOfGroupByResourceId(Integer resourceId) {
        if (resourceId == null)
            return true;
        int taskId = resourcesRepository.findById(resourceId).orElseThrow(NotFoundException::new).getTask().getTaskId();

        return isMemberOfGroupByTaskId(taskId);
    }
}
