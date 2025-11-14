package com.vitalioleksenko.csp.controllers;

import com.vitalioleksenko.csp.models.ActivityLog;
import com.vitalioleksenko.csp.models.Group;
import com.vitalioleksenko.csp.services.ActivitiesLogsService;
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
@RequestMapping("/api/activitylog")
public class ActivitiesLogsController {
    private final ActivitiesLogsService activitiesLogsService;
    private final ModelMapper modelMapper;

    @Autowired
    public ActivitiesLogsController(ActivitiesLogsService activitiesLogsService, ModelMapper modelMapper) {
        this.activitiesLogsService = activitiesLogsService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("")
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid ActivityLog activityLog,
                                             BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new BadRequestException(
                    ErrorBuilder.fromBindingErrors(bindingResult)
            );
        }

        activitiesLogsService.save(activityLog);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("")
    public List<ActivityLog> readAll(){
        return activitiesLogsService.findAll();
    }

    @GetMapping("/{id}")
    public ActivityLog readOne(@PathVariable("id") int id){
        return activitiesLogsService.findById(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable("id") int id,
                                             @RequestBody  @Valid ActivityLog activityLog,
                                             BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new BadRequestException(
                    ErrorBuilder.fromBindingErrors(bindingResult)
            );
        }

        activitiesLogsService.edit(activityLog, id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id){
        activitiesLogsService.remove(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
