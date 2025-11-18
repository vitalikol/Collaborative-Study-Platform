package com.vitalioleksenko.csp.controllers;

import com.vitalioleksenko.csp.dto.GroupDTO;
import com.vitalioleksenko.csp.dto.TaskDTO;
import com.vitalioleksenko.csp.models.Group;
import com.vitalioleksenko.csp.models.Membership;
import com.vitalioleksenko.csp.models.Task;
import com.vitalioleksenko.csp.services.GroupsService;
import com.vitalioleksenko.csp.util.BadRequestException;
import com.vitalioleksenko.csp.util.ErrorBuilder;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/group")
public class GroupsController {
    private final GroupsService groupsService;
    private final ModelMapper modelMapper;

    @Autowired
    public GroupsController(GroupsService groupsService, ModelMapper modelMapper) {
        this.groupsService = groupsService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("")
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid Group group,
                                             BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new BadRequestException(
                    ErrorBuilder.fromBindingErrors(bindingResult)
            );
        }

        groupsService.save(group);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("")
    public List<GroupDTO> readAll(@RequestParam(name = "userId", required = false) Integer userId){
        if(userId != null){
            return groupsService.findAllByMember(userId).stream().map(this::convertToGroupDTO).collect(Collectors.toList());
        }
        return groupsService.findAll().stream().map(this::convertToGroupDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public GroupDTO readOne(@PathVariable("id") int id){
        return convertToGroupDTO(groupsService.findById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable("id") int id,
                                             @RequestBody  @Valid Group group,
                                             BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new BadRequestException(
                    ErrorBuilder.fromBindingErrors(bindingResult)
            );
        }

        groupsService.edit(group, id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id){
        groupsService.remove(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }


    private Group convertToGroup(GroupDTO groupDTO) {
        return modelMapper.map(groupDTO, Group.class);
    }

    private GroupDTO convertToGroupDTO(Group group) {
        return modelMapper.map(group, GroupDTO.class);
    }
}
