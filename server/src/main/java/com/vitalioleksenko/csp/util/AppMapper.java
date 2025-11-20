package com.vitalioleksenko.csp.util;

import com.vitalioleksenko.csp.dto.activity.ActivityLogDetailedDTO;
import com.vitalioleksenko.csp.dto.activity.ActivityLogPartialDTO;
import com.vitalioleksenko.csp.dto.activity.ActivityLogShortDTO;
import com.vitalioleksenko.csp.dto.group.*;
import com.vitalioleksenko.csp.dto.membership.MembershipDTO;
import com.vitalioleksenko.csp.dto.membership.MembershipShortDTO;
import com.vitalioleksenko.csp.dto.resource.ResourceDetailedDTO;
import com.vitalioleksenko.csp.dto.resource.ResourcePartialDTO;
import com.vitalioleksenko.csp.dto.resource.ResourceShortDTO;
import com.vitalioleksenko.csp.dto.task.*;
import com.vitalioleksenko.csp.dto.user.*;
import com.vitalioleksenko.csp.models.*;
import com.vitalioleksenko.csp.repositories.GroupsRepository;
import com.vitalioleksenko.csp.repositories.UsersRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class AppMapper {

    @Autowired
    protected UsersRepository usersRepository;

    @Autowired
    protected GroupsRepository groupsRepository;

    // ================= User =================
    public abstract UserShortDTO toUserShort(User user);
    public abstract UserPartialDTO toUserPartial(User user);
    public abstract UserDetailedDTO toUserDetailed(User user);

    public abstract List<UserShortDTO> toUserShortList(List<User> users);
    public abstract List<UserPartialDTO> toUserPartialList(List<User> users);
    public abstract List<UserDetailedDTO> toUserDetailedList(List<User> users);

    @Mapping(source = "password", target = "passwordHash")
    public abstract User toUser(UserCreateDTO dto);

    @Mapping(source = "password", target = "passwordHash", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "email", target = "email", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "name", target = "name", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "role", target = "role", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updateUserFromDto(UserUpdateDTO dto, @MappingTarget User user);

    // ================= Group =================
    public abstract GroupShortDTO toGroupShort(Group group);
    public abstract GroupPartialDTO toGroupPartial(Group group);
    public abstract GroupDetailedDTO toGroupDetailed(Group group);

    public abstract List<GroupShortDTO> toGroupShortList(List<Group> groups);
    public abstract List<GroupPartialDTO> toGroupPartialList(List<Group> groups);
    public abstract List<GroupDetailedDTO> toGroupDetailedList(List<Group> groups);

    @Mapping(target = "createdBy", expression = "java(getUser(dto.getCreatedBy()))")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    public abstract Group toGroup(GroupCreateDTO dto);

    @Mapping(source = "name", target = "name", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "description", target = "description", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updateGroupFromDto(GroupUpdateDTO dto, @MappingTarget Group group);
    // ================= Task =================
    public abstract TaskShortDTO toTaskShort(Task task);
    public abstract TaskPartialDTO toTaskPartial(Task task);
    public abstract TaskDetailedDTO toTaskDetailed(Task task);

    public abstract List<TaskShortDTO> toTaskShortList(List<Task> tasks);
    public abstract List<TaskPartialDTO> toTaskPartialList(List<Task> tasks);
    public abstract List<TaskDetailedDTO> toTaskDetailedList(List<Task> tasks);

    @Mapping(target = "group", expression = "java(getGroup(dto.getGroupId()))")
    @Mapping(target = "user", expression = "java(getUser(dto.getUserId()))")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "deadline", expression = "java(dto.getDeadline())")
    @Mapping(source = "status", target = "status", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_DEFAULT)
    public abstract Task toTask(TaskCreateDTO dto);

    @Mapping(source = "title", target = "title", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "description", target = "description", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "status", target = "status", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "deadline", target = "deadline", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updateTaskFromDto(TaskUpdateDTO dto, @MappingTarget Task task);

    // ================= Resource =================
    public abstract ResourceShortDTO toResourceShort(Resource resource);
    public abstract ResourcePartialDTO toResourcePartial(Resource resource);
    public abstract ResourceDetailedDTO toResourceDetailed(Resource resource);

    public abstract List<ResourceShortDTO> toResourceShortList(List<Resource> resources);
    public abstract List<ResourcePartialDTO> toResourcePartialList(List<Resource> resources);
    public abstract List<ResourceDetailedDTO> toResourceDetailedList(List<Resource> resources);

    // ================= Membership =================
    public abstract MembershipShortDTO toMembershipShort(Membership membership);
    public abstract MembershipDTO toMembership(Membership membership);

    public abstract List<MembershipShortDTO> toMembershipShortList(List<Membership> memberships);
    public abstract List<MembershipDTO> toMembership(List<Membership> memberships);

    // ================= ActivityLog =================
    public abstract ActivityLogShortDTO toActivityLogShort(ActivityLog log);
    public abstract ActivityLogPartialDTO toActivityLogPartial(ActivityLog log);
    public abstract ActivityLogDetailedDTO toActivityLogDetailed(ActivityLog log);

    public abstract List<ActivityLogShortDTO> toActivityLogShortList(List<ActivityLog> logs);
    public abstract List<ActivityLogPartialDTO> toActivityLogPartialList(List<ActivityLog> logs);
    public abstract List<ActivityLogDetailedDTO> toActivityLogDetailedList(List<ActivityLog> logs);

    // ================= Helper Methods =================
    protected Group getGroup(Integer groupId) {
        if (groupId == null) return null;
        return groupsRepository.findById(groupId).orElseThrow(NotFoundException::new);
    }

    protected User getUser(Integer userId) {
        if (userId == null) return null;
        return usersRepository.findById(userId).orElseThrow(NotFoundException::new);
    }

}

