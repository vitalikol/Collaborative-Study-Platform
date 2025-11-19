package com.vitalioleksenko.csp.controllers;

import com.vitalioleksenko.csp.dto.user.UserDetailedDTO;
import com.vitalioleksenko.csp.dto.user.UserPartialDTO;
import com.vitalioleksenko.csp.repositories.UsersRepository;
import com.vitalioleksenko.csp.util.AuthenticationRequest;
import com.vitalioleksenko.csp.dto.UserDTO;
import com.vitalioleksenko.csp.security.CustomUserDetails;
import com.vitalioleksenko.csp.util.BadRequestException;
import com.vitalioleksenko.csp.util.RegisterRequest;
import com.vitalioleksenko.csp.models.User;
import com.vitalioleksenko.csp.services.UsersService;
import com.vitalioleksenko.csp.util.ErrorResponse;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UsersService usersService;
    private final ModelMapper modelMapper;
    private final UsersRepository usersRepository;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, UsersService usersService, ModelMapper modelMapper, UsersRepository usersRepository) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.usersService = usersService;
        this.modelMapper = modelMapper;
        this.usersRepository = usersRepository;
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
            throw new BadRequestException(errorMsgBuilder(bindingResult));
        }

        User newUser = new User();
        modelMapper.map(request, newUser);
        newUser.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        usersRepository.save(newUser);

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
    public ResponseEntity<UserDetailedDTO> me(@AuthenticationPrincipal CustomUserDetails user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        UserDetailedDTO dto = usersService.findById(user.getId());
        return ResponseEntity.ok(dto);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(BadRequestException e){
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

    private UserDTO convertToUserDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }
}
