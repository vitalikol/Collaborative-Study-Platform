package com.vitalioleksenko.csp.controllers;

import com.vitalioleksenko.csp.models.dto.activity.ActivityLogDetailedDTO;
import com.vitalioleksenko.csp.services.ActivitiesLogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/log")
public class ActivitiesLogsController {
    private final ActivitiesLogsService activitiesLogsService;

    @Autowired
    public ActivitiesLogsController(ActivitiesLogsService activitiesLogsService) {
        this.activitiesLogsService = activitiesLogsService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("")
    public Page<ActivityLogDetailedDTO> readAll(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "20") int size){
        return activitiesLogsService.findAll(page, size);
    }

    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    @GetMapping("/user/{id}")
    public Page<ActivityLogDetailedDTO> readAllForUser(@PathVariable("id") int userId,
                                                      @RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "20") int size){
        return activitiesLogsService.findAllForUser(userId, page, size);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ActivityLogDetailedDTO readOne(@PathVariable("id") int id){
        return activitiesLogsService.findById(id);
    }
}
