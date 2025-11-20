package com.vitalioleksenko.csp.controllers;

import com.vitalioleksenko.csp.dto.activity.ActivityLogDetailedDTO;
import com.vitalioleksenko.csp.dto.activity.ActivityLogPartialDTO;
import com.vitalioleksenko.csp.services.ActivitiesLogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/activitylog")
public class ActivitiesLogsController {
    private final ActivitiesLogsService activitiesLogsService;

    @Autowired
    public ActivitiesLogsController(ActivitiesLogsService activitiesLogsService) {
        this.activitiesLogsService = activitiesLogsService;
    }

    @GetMapping("")
    public Page<ActivityLogPartialDTO> readAll(@RequestParam(name = "userId", required = false) Integer userId,
                                               @RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "20") int size){
        return activitiesLogsService.findAll(userId, page, size);
    }

    @GetMapping("/{id}")
    public ActivityLogDetailedDTO readOne(@PathVariable("id") int id){
        return activitiesLogsService.findById(id);
    }
}
