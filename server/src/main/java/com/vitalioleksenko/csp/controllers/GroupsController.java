package com.vitalioleksenko.csp.controllers;

import com.vitalioleksenko.csp.dto.group.GroupDetailedDTO;
import com.vitalioleksenko.csp.dto.group.GroupPartialDTO;
import com.vitalioleksenko.csp.models.Group;
import com.vitalioleksenko.csp.services.GroupsService;
import com.vitalioleksenko.csp.util.AppMapper;
import com.vitalioleksenko.csp.util.BadRequestException;
import com.vitalioleksenko.csp.util.ErrorBuilder;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/group")
public class GroupsController {
    private final GroupsService groupsService;
    private final AppMapper mapper;


    @Autowired
    public GroupsController(GroupsService groupsService, @Qualifier("appMapperImpl") AppMapper mapper) {
        this.groupsService = groupsService;
        this.mapper = mapper;
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
    public List<GroupPartialDTO> readAll(@RequestParam(name = "userId", required = false) Integer userId){
        if(userId != null){
            return mapper.toGroupPartialList(groupsService.findAllByMember(userId));
        }
        return mapper.toGroupPartialList(groupsService.findAll());
    }

    @GetMapping("/{id}")
    public GroupDetailedDTO readOne(@PathVariable("id") int id){
        return mapper.toGroupDetailed(groupsService.findById(id));
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

}
