package com.vitalioleksenko.csp.services;

import com.vitalioleksenko.csp.models.dto.group.GroupCreateDTO;
import com.vitalioleksenko.csp.models.dto.group.GroupDetailedDTO;
import com.vitalioleksenko.csp.models.dto.group.GroupPartialDTO;
import com.vitalioleksenko.csp.models.dto.group.GroupUpdateDTO;
import com.vitalioleksenko.csp.models.dto.membership.MembershipCreateDTO;
import com.vitalioleksenko.csp.models.Group;
import com.vitalioleksenko.csp.repositories.GroupsRepository;
import com.vitalioleksenko.csp.util.AppMapper;
import com.vitalioleksenko.csp.models.enums.GroupRole;
import com.vitalioleksenko.csp.util.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class GroupsService {
    private final GroupsRepository groupsRepository;
    private final AppMapper mapper;
    private final ActivitiesLogsService activitiesLogsService;
    private final MembershipsService membershipsService;

    @Autowired
    public GroupsService(GroupsRepository groupsRepository, @Qualifier("appMapperImpl") AppMapper appMapper, ActivitiesLogsService activitiesLogsService, MembershipsService membershipsService) {
        this.groupsRepository = groupsRepository;
        this.mapper = appMapper;
        this.activitiesLogsService = activitiesLogsService;
        this.membershipsService = membershipsService;
    }

    @Transactional
    public void save(GroupCreateDTO dto){
        Group group = mapper.toGroup(dto);
        groupsRepository.save(group);
        membershipsService.save(new MembershipCreateDTO(group.getCreatedBy().getUserId(), group.getGroupId(), GroupRole.TEAM_LEAD));
        activitiesLogsService.log(
                "GROUP_CREATED",
                "Created group with ID: " + group.getGroupId()
        );
    }

    public Page<GroupPartialDTO> getGroups(String search, Integer userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Group> result;

        if (userId != null && search != null && !search.isBlank()) {
            result = groupsRepository.findByMembersUserUserIdAndNameContainingIgnoreCase(userId, search, pageable);
        } else if (userId != null) {
            result = groupsRepository.findByMembersUserUserId(userId, pageable);
        } else if (search != null && !search.isBlank()) {
            result = groupsRepository.findByNameContainingIgnoreCase(search, pageable);
        } else {
            result = groupsRepository.findAll(pageable);
        }

        return result.map(mapper::toGroupPartial);
    }

    public GroupDetailedDTO getById(int id){
        Group group = groupsRepository.findById(id).orElseThrow(NotFoundException::new);
        return mapper.toGroupDetailed(group);
    }

    @Transactional
    public void edit(GroupUpdateDTO dto, int id){
        Group group = groupsRepository.findById(id).orElseThrow(NotFoundException::new);
        mapper.updateGroupFromDto(dto, group);
        groupsRepository.save(group);

        activitiesLogsService.log(
                "GROUP_EDITED",
                "Edited group with ID: " + group.getGroupId()
        );
    }


    @Transactional
    public boolean remove(int id){
        return groupsRepository.findById(id).map(group -> {
            groupsRepository.delete(group);
            activitiesLogsService.log(
                    "GROUP_DELETED",
                    "Deleted group with ID: " + id
            );
            return true;
        }).orElse(false);
    }
}
