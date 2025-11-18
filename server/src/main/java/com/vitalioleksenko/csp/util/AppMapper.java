package com.vitalioleksenko.csp.util;

import com.vitalioleksenko.csp.dto.activity.ActivityLogDetailedDTO;
import com.vitalioleksenko.csp.dto.activity.ActivityLogPartialDTO;
import com.vitalioleksenko.csp.dto.activity.ActivityLogShortDTO;
import com.vitalioleksenko.csp.dto.group.GroupDetailedDTO;
import com.vitalioleksenko.csp.dto.group.GroupPartialDTO;
import com.vitalioleksenko.csp.dto.group.GroupShortDTO;
import com.vitalioleksenko.csp.dto.membership.MembershipDTO;
import com.vitalioleksenko.csp.dto.membership.MembershipShortDTO;
import com.vitalioleksenko.csp.dto.resource.ResourceDetailedDTO;
import com.vitalioleksenko.csp.dto.resource.ResourcePartialDTO;
import com.vitalioleksenko.csp.dto.resource.ResourceShortDTO;
import com.vitalioleksenko.csp.dto.task.TaskDetailedDTO;
import com.vitalioleksenko.csp.dto.task.TaskPartialDTO;
import com.vitalioleksenko.csp.dto.task.TaskShortDTO;
import com.vitalioleksenko.csp.dto.user.UserDetailedDTO;
import com.vitalioleksenko.csp.dto.user.UserShortDTO;
import com.vitalioleksenko.csp.models.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AppMapper {

    // ================= User =================
    UserShortDTO toUserShort(User user);

    @Mapping(target = "createdGroups", source = "createdGroups")
    @Mapping(target = "tasks", source = "tasks")
    @Mapping(target = "resources", source = "resources")
    @Mapping(target = "activities", source = "activities")
    UserDetailedDTO toUserDetailed(User user);

    List<UserShortDTO> toUserShortList(List<User> users);
    List<UserDetailedDTO> toUserDetailedList(List<User> users);

    // ================= Group =================
    GroupShortDTO toGroupShort(Group group);
    GroupPartialDTO toGroupPartial(Group group);
    GroupDetailedDTO toGroupDetailed(Group group);

    List<GroupShortDTO> toGroupShortList(List<Group> groups);
    List<GroupPartialDTO> toGroupPartialList(List<Group> groups);
    List<GroupDetailedDTO> toGroupDetailedList(List<Group> groups);

    // ================= Task =================
    TaskShortDTO toTaskShort(Task task);
    TaskPartialDTO toTaskPartial(Task task);
    TaskDetailedDTO toTaskDetailed(Task task);

    List<TaskShortDTO> toTaskShortList(List<Task> tasks);
    List<TaskPartialDTO> toTaskPartialList(List<Task> tasks);
    List<TaskDetailedDTO> toTaskDetailedList(List<Task> tasks);

    // ================= Resource =================
    ResourceShortDTO toResourceShort(Resource resource);
    ResourcePartialDTO toResourcePartial(Resource resource);
    ResourceDetailedDTO toResourceDetailed(Resource resource);

    List<ResourceShortDTO> toResourceShortList(List<Resource> resources);
    List<ResourcePartialDTO> toResourcePartialList(List<Resource> resources);
    List<ResourceDetailedDTO> toResourceDetailedList(List<Resource> resources);

    // ================= Membership =================
    MembershipShortDTO toMembershipShort(Membership membership);
    MembershipDTO toMembership(Membership membership);

    List<MembershipShortDTO> toMembershipShortList(List<Membership> memberships);
    List<MembershipDTO> toMembership(List<Membership> memberships);

    // ================= ActivityLog =================
    ActivityLogShortDTO toActivityLogShort(ActivityLog log);
    ActivityLogPartialDTO toActivityLogPartial(ActivityLog log);
    ActivityLogDetailedDTO toActivityLogDetailed(ActivityLog log);

    List<ActivityLogShortDTO> toActivityLogShortList(List<ActivityLog> logs);
    List<ActivityLogPartialDTO> toActivityLogPartialList(List<ActivityLog> logs);
    List<ActivityLogDetailedDTO> toActivityLogDetailedList(List<ActivityLog> logs);
}
