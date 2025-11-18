package com.vitalioleksenko.csp.controllers;

import com.vitalioleksenko.csp.dto.UserDTO;
import com.vitalioleksenko.csp.dto.user.UserDetailedDTO;
import com.vitalioleksenko.csp.util.AppMapper;
import com.vitalioleksenko.csp.util.BadRequestException;
import com.vitalioleksenko.csp.models.User;
import com.vitalioleksenko.csp.services.UsersService;
import com.vitalioleksenko.csp.util.ErrorBuilder;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class UsersController {
    private final UsersService usersService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final AppMapper mapper;

    @Autowired
    public UsersController(UsersService usersService, PasswordEncoder passwordEncoder, ModelMapper modelMapper, AppMapper mapper) {
        this.usersService = usersService;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.mapper = mapper;
    }

    @PostMapping("")
    public ResponseEntity<HttpStatus> create(@RequestBody  @Valid User user,
                                             BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new BadRequestException(
                ErrorBuilder.fromBindingErrors(bindingResult)
            );
        }
        String passwordHash = passwordEncoder.encode(user.getPasswordHash());
        user.setPasswordHash(passwordHash);

        usersService.save(user);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("")
    public List<UserDTO> readAll(){
        return usersService.findAll().stream().map(this::convertToUserDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UserDetailedDTO readOne(@PathVariable("id") int id){
        return mapper.toUserDetailed(usersService.findById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable("id") int id,
                                             @RequestBody  @Valid User user,
                                             BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new BadRequestException(
                ErrorBuilder.fromBindingErrors(bindingResult)
            );
        }
        String passwordHash = passwordEncoder.encode(user.getPasswordHash());
        user.setPasswordHash(passwordHash);

        usersService.edit(user, id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id){
        usersService.remove(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    private UserDetailedDTO convertToUserDetailedDTO(User user) {
        return modelMapper.map(user, UserDetailedDTO.class);
    }

    private User convertToUser(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    private UserDTO convertToUserDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }
}
