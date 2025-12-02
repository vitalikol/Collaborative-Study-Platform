package com.vitalioleksenko.csp.services;

import com.vitalioleksenko.csp.models.Group;
import com.vitalioleksenko.csp.models.dto.membership.MembershipCreateDTO;
import com.vitalioleksenko.csp.models.dto.membership.MembershipUpdateDTO;
import com.vitalioleksenko.csp.models.Membership;
import com.vitalioleksenko.csp.repositories.GroupsRepository;
import com.vitalioleksenko.csp.repositories.MembershipsRepository;
import com.vitalioleksenko.csp.services.notification.NotificationService;
import com.vitalioleksenko.csp.util.AppMapper;
import com.vitalioleksenko.csp.util.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MembershipsService {
    private final MembershipsRepository membershipRepository;
    private final AppMapper mapper;
    private final ActivitiesLogsService activitiesLogsService;
    private final NotificationService notificationService;
    private final GroupsRepository groupsRepository;

    @Autowired
    public MembershipsService(MembershipsRepository membershipRepository, @Qualifier("appMapperImpl")  AppMapper mapper, ActivitiesLogsService activitiesLogsService, NotificationService notificationService, GroupsRepository groupsRepository) {
        this.membershipRepository = membershipRepository;
        this.mapper = mapper;
        this.activitiesLogsService = activitiesLogsService;
        this.notificationService = notificationService;
        this.groupsRepository = groupsRepository;
    }

    @Transactional
    public void save(MembershipCreateDTO dto){
        Membership membership = mapper.toMembership(dto);
        membershipRepository.save(membership);
        Group group = groupsRepository.findById(dto.getGroupId()).orElseThrow(NotFoundException::new);
        notificationService.notifyGroupInvitation(group, dto.getUserId());
        activitiesLogsService.log(
                "MEMBER_ADDED",
                "Member with ID: " + membership.getUser().getUserId() + " joined group with ID: " + membership.getMembershipId()
        );
    }

    @Transactional
    public void edit(MembershipUpdateDTO dto, int id){
        Membership membership = membershipRepository.findById(id).orElseThrow(NotFoundException::new);
        mapper.updateMembershipFromDto(dto, membership);
        membershipRepository.save(membership);
        activitiesLogsService.log(
                "MEMBERSHIP_EDITED",
                "Edited membership with ID: " + membership.getMembershipId()
        );
    }

    @Transactional
    public boolean remove(int id){
        return membershipRepository.findById(id).map(membership -> {
            membershipRepository.delete(membership);
            activitiesLogsService.log(
                    "MEMBER_LEFT",
                    "Member with ID: " + membership.getUser().getUserId() +" left the group with ID: " + id
            );
            return true;
        }).orElse(false);
    }
}
