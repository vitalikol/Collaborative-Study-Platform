package com.vitalioleksenko.csp.controllers;

import com.vitalioleksenko.csp.models.Group;
import com.vitalioleksenko.csp.models.Membership;
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
    public List<Group> readAll(){
        return groupsService.findAll();
    }

    @GetMapping("/{id}")
    public Group readOne(@PathVariable("id") int id){
        return groupsService.findById(id);
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
