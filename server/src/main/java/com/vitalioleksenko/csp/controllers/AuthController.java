package com.vitalioleksenko.csp.controllers;

import com.vitalioleksenko.csp.models.dto.JwtResponse;
import com.vitalioleksenko.csp.models.dto.user.AuthenticationRequest;
import com.vitalioleksenko.csp.models.dto.user.CustomOAuthUser;
import com.vitalioleksenko.csp.models.dto.user.RegisterRequest;
import com.vitalioleksenko.csp.models.dto.user.UserDetailedDTO;
import com.vitalioleksenko.csp.models.User;
import com.vitalioleksenko.csp.repositories.UsersRepository;
import com.vitalioleksenko.csp.security.CustomUserDetails;
import com.vitalioleksenko.csp.security.Role;
import com.vitalioleksenko.csp.services.ActivitiesLogsService;
import com.vitalioleksenko.csp.services.AuthService;
import com.vitalioleksenko.csp.services.JwtService;
import com.vitalioleksenko.csp.services.UsersService;
import com.vitalioleksenko.csp.util.AppMapper;
import com.vitalioleksenko.csp.util.exceptions.BadRequestException;
import com.vitalioleksenko.csp.util.exceptions.ErrorBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.management.remote.JMXAuthenticator;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UsersService usersService;
    private final AuthService authService;

    @Autowired
    public AuthController(UsersService usersService, AuthService authService) {
        this.usersService = usersService;
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest authenticationRequest) {
        return ResponseEntity.ok(authService.login(authenticationRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<HttpStatus> register(@RequestBody @Valid RegisterRequest registerRequest,
                                               BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new BadRequestException(
                    ErrorBuilder.fromBindingErrors(bindingResult)
            );
        }
        authService.register(registerRequest);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @PostMapping("/logout")
    public ResponseEntity<HttpStatus> logout(HttpServletRequest request) {
        //TODO
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDetailedDTO> me(@AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(usersService.findById(user.getId()));
    }
}
