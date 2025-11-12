package com.vitalioleksenko.csp.controllers;

import com.vitalioleksenko.csp.dto.UserDTO;
import com.vitalioleksenko.csp.util.BadUserException;
import com.vitalioleksenko.csp.models.User;
import com.vitalioleksenko.csp.services.UsersService;
import com.vitalioleksenko.csp.util.ErrorResponse;
import com.vitalioleksenko.csp.util.UserNotFoundException;
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

@RestController
@RequestMapping("/user")
public class UsersController {
    private final UsersService usersService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Autowired
    public UsersController(UsersService usersService, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.usersService = usersService;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    @PostMapping("")
    public ResponseEntity<HttpStatus> create(@RequestBody  @Valid User user,
                                             BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new BadUserException(errorMsgBuilder(bindingResult));
        }
        String passwordHash = passwordEncoder.encode(user.getPasswordHash());
        user.setPasswordHash(passwordHash);

        usersService.save(user);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("")
    public List<User> readAll(){
        return usersService.findAll();
    }

    @GetMapping("/{id}")
    public User readOne(@PathVariable("id") int id){
        return usersService.findOne(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable("id") int id,
                                             @RequestBody  @Valid User user,
                                             BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new BadUserException(errorMsgBuilder(bindingResult));
        }
        usersService.edit(user, id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id){
        usersService.remove(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(UserNotFoundException e){
        ErrorResponse errorResponse = new ErrorResponse(
                "User not found",
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(BadUserException e){
        ErrorResponse ErrorResponse = new ErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(ErrorResponse, HttpStatus.BAD_REQUEST);
    }

    private String errorMsgBuilder(BindingResult bindingResult){
        StringBuilder errorMsg = new StringBuilder();

        List<FieldError> errors = bindingResult.getFieldErrors();
        for(FieldError error : errors){
            errorMsg.append(error.getField())
                    .append(" - ")
                    .append(error.getDefaultMessage())
                    .append(";");
        }
        return errorMsg.toString();
    }

    private User convertToUser(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    private UserDTO convertToUserDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }
}
