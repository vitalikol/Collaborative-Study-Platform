package com.vitalioleksenko.csp.services;

import com.vitalioleksenko.csp.models.dto.membership.MembershipUpdateDTO;
import com.vitalioleksenko.csp.models.enums.GroupRole;
import com.vitalioleksenko.csp.repositories.GroupsRepository;
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
    private final GroupsRepository groupsRepository;
    private final TasksRepository tasksRepository;
    private final ResourcesRepository resourcesRepository;
    private final MembershipsRepository membershipsRepository;

    public AccessService(GroupsRepository groupRepository, TasksRepository tasksRepository, ResourcesRepository resourcesRepository, MembershipsRepository membershipsRepository) {
        this.groupsRepository = groupRepository;
        this.tasksRepository = tasksRepository;
        this.resourcesRepository = resourcesRepository;
        this.membershipsRepository = membershipsRepository;
    }

    public boolean hasGroupRole(Integer groupId, GroupRole role){
        if (groupId == null)
            return false;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();

        int userId = user.getId();

        return membershipsRepository.existsByGroupGroupIdAndUserUserIdAndRole(groupId, userId, role);
    }

    public boolean isTeamLead(Integer groupId){
        return hasGroupRole(groupId, GroupRole.TEAM_LEAD);
    }

    public boolean isMemberOfGroup(Integer groupId) {
        if (groupId == null)
            return true;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();

        int userId = user.getId();

        return groupsRepository.existsByGroupIdAndMembersUserUserId(groupId, userId);
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
