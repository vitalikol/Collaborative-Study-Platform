package com.vitalioleksenko.csp.controllers;

import com.vitalioleksenko.csp.dto.task.TaskDetailedDTO;
import com.vitalioleksenko.csp.dto.task.TaskPartialDTO;
import com.vitalioleksenko.csp.models.Task;
import com.vitalioleksenko.csp.services.TasksService;
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
@RequestMapping("/api/task")
public class TasksController {
    private final TasksService tasksService;
    private final AppMapper mapper;

    @Autowired
    public TasksController(TasksService tasksService, @Qualifier("appMapperImpl") AppMapper mapper) {
        this.tasksService = tasksService;
        this.mapper = mapper;
    }

    @PostMapping("")
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid Task task,
                                             BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new BadRequestException(
                    ErrorBuilder.fromBindingErrors(bindingResult)
            );
        }

        tasksService.save(task);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("")
    public List<TaskPartialDTO> readAll(
            @RequestParam(name = "groupId", required = false) Integer groupId,
            @RequestParam(name = "userId", required = false) Integer userId) {

        if (groupId != null && userId != null) {
            return mapper.toTaskPartialList(tasksService.findByUserAndGroup(userId, groupId));
        }
        if (groupId != null) {
            return mapper.toTaskPartialList(tasksService.findByGroup(groupId));
        }
        if (userId != null) {
            return mapper.toTaskPartialList(tasksService.findByUser(userId));
        }
        return mapper.toTaskPartialList(tasksService.findAll());
    }

    @GetMapping("/{id}")
    public TaskDetailedDTO readOne(@PathVariable("id") int id){
        return mapper.toTaskDetailed(tasksService.findById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable("id") int id,
                                             @RequestBody  @Valid Task task,
                                             BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new BadRequestException(
                    ErrorBuilder.fromBindingErrors(bindingResult)
            );
        }

        tasksService.edit(task, id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id){
        tasksService.remove(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}