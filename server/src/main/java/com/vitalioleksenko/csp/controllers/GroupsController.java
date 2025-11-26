package com.vitalioleksenko.csp.controllers;

import com.vitalioleksenko.csp.models.dto.group.GroupCreateDTO;
import com.vitalioleksenko.csp.models.dto.group.GroupDetailedDTO;
import com.vitalioleksenko.csp.models.dto.group.GroupPartialDTO;
import com.vitalioleksenko.csp.models.dto.group.GroupUpdateDTO;
import com.vitalioleksenko.csp.services.GroupsService;
import com.vitalioleksenko.csp.util.exceptions.BadRequestException;
import com.vitalioleksenko.csp.util.exceptions.ErrorBuilder;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/group")
public class GroupsController {
    private final GroupsService groupsService;

    @Autowired
    public GroupsController(GroupsService groupsService) {
        this.groupsService = groupsService;
    }

    @PostMapping("")
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid GroupCreateDTO dto,
                                             BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new BadRequestException(
                    ErrorBuilder.fromBindingErrors(bindingResult)
            );
        }

        groupsService.save(dto);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    @GetMapping("")
    public Page<GroupPartialDTO> readAll(@RequestParam(name = "search", required = false) String search,
                                         @RequestParam(name = "userId", required = false) Integer userId,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "20") int size){
        return groupsService.getGroups(search, userId, page, size);
    }

    @PreAuthorize("hasRole('ADMIN') or #id == @accessService.isMemberOfGroup(#id)")
    @GetMapping("/{id}")
    public GroupDetailedDTO readOne(@PathVariable("id") int id){
        return groupsService.getById(id);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @accessService.isTeamLead(#id)")
    public ResponseEntity<HttpStatus> update(@PathVariable("id") int id,
                                             @RequestBody  @Valid GroupUpdateDTO dto,
                                             BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new BadRequestException(
                    ErrorBuilder.fromBindingErrors(bindingResult)
            );
        }

        groupsService.edit(dto, id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == @accessService.isTeamLead(#id)")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id){
        groupsService.remove(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
