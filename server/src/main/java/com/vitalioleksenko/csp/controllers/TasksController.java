package com.vitalioleksenko.csp.controllers;

import com.vitalioleksenko.csp.dto.task.TaskCreateDTO;
import com.vitalioleksenko.csp.dto.task.TaskDetailedDTO;
import com.vitalioleksenko.csp.dto.task.TaskPartialDTO;
import com.vitalioleksenko.csp.dto.task.TaskUpdateDTO;
import com.vitalioleksenko.csp.models.Task;
import com.vitalioleksenko.csp.services.TasksService;
import com.vitalioleksenko.csp.util.AppMapper;
import com.vitalioleksenko.csp.util.BadRequestException;
import com.vitalioleksenko.csp.util.ErrorBuilder;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/task")
public class TasksController {
    private final TasksService tasksService;

    @Autowired
    public TasksController(TasksService tasksService) {
        this.tasksService = tasksService;
    }

    @PostMapping("")
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid TaskCreateDTO dto,
                                             BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new BadRequestException(
                    ErrorBuilder.fromBindingErrors(bindingResult)
            );
        }

        tasksService.save(dto);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("")
    public Page<TaskPartialDTO> readAll(
            @RequestParam(required = false) Integer groupId,
            @RequestParam(required = false) Integer userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return tasksService.getTasks(groupId, userId, page, size);
    }

    @GetMapping("/{id}")
    public TaskDetailedDTO readOne(@PathVariable("id") int id){
        return tasksService.getById(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable("id") int id,
                                             @RequestBody  @Valid TaskUpdateDTO dto,
                                             BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new BadRequestException(
                    ErrorBuilder.fromBindingErrors(bindingResult)
            );
        }

        tasksService.edit(dto, id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id){
        tasksService.remove(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}