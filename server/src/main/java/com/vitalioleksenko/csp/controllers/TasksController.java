package com.vitalioleksenko.csp.controllers;

import com.vitalioleksenko.csp.dto.TaskDTO;
import com.vitalioleksenko.csp.dto.UserDTO;
import com.vitalioleksenko.csp.models.Task;
import com.vitalioleksenko.csp.models.User;
import com.vitalioleksenko.csp.security.CustomUserDetails;
import com.vitalioleksenko.csp.services.TasksService;
import com.vitalioleksenko.csp.util.BadRequestException;
import com.vitalioleksenko.csp.util.ErrorBuilder;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/task")
public class TasksController {
    private final TasksService tasksService;
    private final ModelMapper modelMapper;

    @Autowired
    public TasksController(TasksService tasksService, ModelMapper modelMapper) {
        this.tasksService = tasksService;
        this.modelMapper = modelMapper;
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
    public List<TaskDTO> readAll(
            @RequestParam(name = "groupId", required = false) Integer groupId,
            @RequestParam(name = "userId", required = false) Integer userId) {

        if (groupId != null && userId != null) {
            return tasksService.findByUserAndGroup(userId, groupId)
                    .stream().map(this::convertToTaskDTO).toList();
        }
        if (groupId != null) {
            return tasksService.findByGroup(groupId)
                    .stream().map(this::convertToTaskDTO).toList();
        }
        if (userId != null) {
            return tasksService.findByUser(userId)
                    .stream().map(this::convertToTaskDTO).toList();
        }

        return tasksService.findAll()
                .stream().map(this::convertToTaskDTO).toList();
    }

    @GetMapping("/{id}")
    public TaskDTO readOne(@PathVariable("id") int id){
        return convertToTaskDTO(tasksService.findById(id));
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


    private Task convertToTask(TaskDTO taskDTO) {
        return modelMapper.map(taskDTO, Task.class);
    }

    private TaskDTO convertToTaskDTO(Task task) {
        return modelMapper.map(task, TaskDTO.class);
    }
}