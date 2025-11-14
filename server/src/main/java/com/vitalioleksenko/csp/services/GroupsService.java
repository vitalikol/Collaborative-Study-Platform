package com.vitalioleksenko.csp.services;

import com.vitalioleksenko.csp.models.Group;
import com.vitalioleksenko.csp.repositories.GroupsRepository;
import com.vitalioleksenko.csp.util.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class GroupsService {
    private final GroupsRepository groupsRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public GroupsService(GroupsRepository groupsRepository, ModelMapper modelMapper) {
        this.groupsRepository = groupsRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public void save(Group group){
        groupsRepository.save(group);
    }

    public List<Group> findAll(){
        return groupsRepository.findAll();
    }

    public Group findById(int id){
        return groupsRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    @Transactional
    public void edit(Group updatedGroup, int id){
        Group group = groupsRepository.findById(id).orElseThrow(NotFoundException::new);
        modelMapper.map(updatedGroup, group);
        groupsRepository.save(group);
    }

    @Transactional
    public void remove(int id){
        groupsRepository.deleteById(id);
    }
}
