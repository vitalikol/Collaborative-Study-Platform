package com.vitalioleksenko.csp.services;

import com.vitalioleksenko.csp.dto.group.GroupCreateDTO;
import com.vitalioleksenko.csp.dto.group.GroupDetailedDTO;
import com.vitalioleksenko.csp.dto.group.GroupPartialDTO;
import com.vitalioleksenko.csp.dto.group.GroupUpdateDTO;
import com.vitalioleksenko.csp.models.Group;
import com.vitalioleksenko.csp.models.Task;
import com.vitalioleksenko.csp.repositories.GroupsRepository;
import com.vitalioleksenko.csp.util.AppMapper;
import com.vitalioleksenko.csp.util.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class GroupsService {
    private final GroupsRepository groupsRepository;
    private final AppMapper mapper;
    private final ActivitiesLogsService activitiesLogsService;

    @Autowired
    public GroupsService(GroupsRepository groupsRepository, @Qualifier("appMapperImpl") AppMapper appMapper, ActivitiesLogsService activitiesLogsService) {
        this.groupsRepository = groupsRepository;
        this.mapper = appMapper;
        this.activitiesLogsService = activitiesLogsService;
    }

    @Transactional
    public void save(GroupCreateDTO dto){
        Group group = mapper.toGroup(dto);
        Group saved = groupsRepository.save(group);
        activitiesLogsService.log(
                "GROUP_CREATED",
                "Created group with ID: " + group.getGroupId()
        );
    }

    public Page<GroupPartialDTO> getGroups(Integer userId, int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<Group> result;
        if(userId != null){
            result = groupsRepository.findAllByUserId(userId, pageable);
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
                "GROUP_UPDATED",
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
