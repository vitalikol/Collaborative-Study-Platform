package com.vitalioleksenko.csp.controllers;

import com.vitalioleksenko.csp.dto.activity.ActivityLogDetailedDTO;
import com.vitalioleksenko.csp.dto.activity.ActivityLogPartialDTO;
import com.vitalioleksenko.csp.models.ActivityLog;
import com.vitalioleksenko.csp.models.Group;
import com.vitalioleksenko.csp.services.ActivitiesLogsService;
import com.vitalioleksenko.csp.services.GroupsService;
import com.vitalioleksenko.csp.util.AppMapper;
import com.vitalioleksenko.csp.util.BadRequestException;
import com.vitalioleksenko.csp.util.ErrorBuilder;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    private final AppMapper mapper;


    @Autowired
    public ActivitiesLogsController(ActivitiesLogsService activitiesLogsService, ModelMapper modelMapper, @Qualifier("appMapperImpl")AppMapper mapper) {
        this.activitiesLogsService = activitiesLogsService;
        this.modelMapper = modelMapper;
        this.mapper = mapper;
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
    public List<ActivityLogPartialDTO> readAll(){
        return mapper.toActivityLogPartialList(activitiesLogsService.findAll());
    }

    @GetMapping("/{id}")
    public ActivityLogDetailedDTO readOne(@PathVariable("id") int id){
        return mapper.toActivityLogDetailed(activitiesLogsService.findById(id));
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
