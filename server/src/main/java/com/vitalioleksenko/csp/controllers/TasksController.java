package com.vitalioleksenko.csp.controllers;

import com.vitalioleksenko.csp.models.dto.task.TaskCreateDTO;
import com.vitalioleksenko.csp.models.dto.task.TaskDetailedDTO;
import com.vitalioleksenko.csp.models.dto.task.TaskPartialDTO;
import com.vitalioleksenko.csp.models.dto.task.TaskUpdateDTO;
import com.vitalioleksenko.csp.models.enums.TaskStatus;
import com.vitalioleksenko.csp.services.TasksService;
import com.vitalioleksenko.csp.util.exceptions.BadRequestException;
import com.vitalioleksenko.csp.util.exceptions.ErrorBuilder;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/task")
public class TasksController {

    private final TasksService tasksService;

    @Autowired
    public TasksController(TasksService tasksService) {
        this.tasksService = tasksService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDetailedDTO create(@RequestBody @Valid TaskCreateDTO dto,
                                  BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(
                    ErrorBuilder.fromBindingErrors(bindingResult)
            );
        }

        return tasksService.save(dto);
    }

    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id  or @accessService.isMemberOfGroup(#groupId)")
    @GetMapping
    public Page<TaskPartialDTO> readAll(
            @RequestParam(required = false) Integer groupId,
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        return tasksService.getTasks(groupId, userId, status, page, size);
    }

    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id  or @accessService.isMemberOfGroup(#groupId)")
    @GetMapping("/active")
    public Page<TaskPartialDTO> getActiveTasks(
            @RequestParam(required = false) Integer groupId,
            @RequestParam(required = false) Integer userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return tasksService.getActiveTasks(groupId, userId, page, size);
    }

    @PreAuthorize("hasRole('ADMIN') or @accessService.isMemberOfGroupByTaskId(#id)")
    @GetMapping("/{id}")
    public TaskDetailedDTO readOne(@PathVariable("id") int id) {
        return tasksService.getById(id);
    }

    @PreAuthorize("hasRole('ADMIN') or @accessService.isMemberOfGroupByTaskId(#id)")
    @PatchMapping("/{id}")
    public TaskDetailedDTO update(@PathVariable("id") int id,
                                  @RequestBody @Valid TaskUpdateDTO dto,
                                  BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(
                    ErrorBuilder.fromBindingErrors(bindingResult)
            );
        }

        return tasksService.edit(dto, id);
    }

    @PreAuthorize("hasRole('ADMIN') or @accessService.isMemberOfGroupByTaskId(#id)")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") int id) {
        tasksService.remove(id);
    }
}
