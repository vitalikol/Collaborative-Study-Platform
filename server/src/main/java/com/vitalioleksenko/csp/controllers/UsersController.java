package com.vitalioleksenko.csp.controllers;

import com.vitalioleksenko.csp.models.dto.UserStats;
import com.vitalioleksenko.csp.models.dto.user.UserCreateDTO;
import com.vitalioleksenko.csp.models.dto.user.UserDetailedDTO;
import com.vitalioleksenko.csp.models.dto.user.UserPartialDTO;
import com.vitalioleksenko.csp.models.dto.user.UserUpdateDTO;
import com.vitalioleksenko.csp.services.TasksService;
import com.vitalioleksenko.csp.services.UsersService;
import com.vitalioleksenko.csp.util.exceptions.BadRequestException;
import com.vitalioleksenko.csp.util.exceptions.ErrorBuilder;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UsersController {
    private final UsersService usersService;
    private final TasksService tasksService;

    @Autowired
    public UsersController(UsersService usersService, TasksService tasksService) {
        this.usersService = usersService;
        this.tasksService = tasksService;
    }

    @PostMapping("")
    public ResponseEntity<HttpStatus> create(@RequestBody  @Valid UserCreateDTO dto,
                                             BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new BadRequestException(
                ErrorBuilder.fromBindingErrors(bindingResult)
            );
        }
        usersService.save(dto);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("")
    public Page<UserPartialDTO> readAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String search) {
        return usersService.getUsers(page, size, search);
    }

    @GetMapping("/{id}")
    public UserDetailedDTO readOne(@PathVariable("id") int id){
        return usersService.findById(id);
    }

    @GetMapping("/{id}/stats")
    public UserStats readStats(@PathVariable("id") int id){
        return tasksService.getStatsForUser(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable("id") int id,
                                             @RequestBody  @Valid UserUpdateDTO dto,
                                             BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new BadRequestException(
                ErrorBuilder.fromBindingErrors(bindingResult)
            );
        }
        usersService.edit(dto, id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        usersService.remove(id);

        return ResponseEntity.noContent().build();
    }
}
