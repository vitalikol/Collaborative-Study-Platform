package com.vitalioleksenko.csp.controllers;

import com.vitalioleksenko.csp.dto.AuthenticationRequest;
import com.vitalioleksenko.csp.util.BadUserException;
import com.vitalioleksenko.csp.dto.RegisterRequest;
import com.vitalioleksenko.csp.models.User;
import com.vitalioleksenko.csp.services.UsersService;
import com.vitalioleksenko.csp.util.ErrorResponse;
import com.vitalioleksenko.csp.util.UserNotFoundException;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UsersService usersService;
    private final ModelMapper modelMapper;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, UsersService usersService, ModelMapper modelMapper) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.usersService = usersService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/login")
    public ResponseEntity<HttpStatus>  login(@RequestBody AuthenticationRequest authenticationRequest,
                                             HttpServletRequest request) {


        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        HttpSession session = request.getSession(true);
        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<HttpStatus> register(@RequestBody @Valid RegisterRequest request,
                                               BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            throw new BadUserException(errorMsgBuilder(bindingResult));
        }

        User newUser = new User();
        modelMapper.map(request, newUser);
        newUser.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        usersService.save(newUser);

        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @PostMapping("/logout")
    public ResponseEntity<HttpStatus> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/me")
    public Object me(Authentication authentication) {
        return authentication != null ? authentication.getPrincipal() : "Anonymous";
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(BadUserException e){
        ErrorResponse ErrorResponse = new ErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(ErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(BadCredentialsException e){
        ErrorResponse errorResponse = new ErrorResponse(
                "Wrong username or password",
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
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
}
