package com.vitalioleksenko.csp.controllers;

import com.vitalioleksenko.csp.dto.user.AuthenticationRequest;
import com.vitalioleksenko.csp.dto.user.RegisterRequest;
import com.vitalioleksenko.csp.dto.user.UserCreateDTO;
import com.vitalioleksenko.csp.dto.user.UserDetailedDTO;
import com.vitalioleksenko.csp.models.User;
import com.vitalioleksenko.csp.repositories.UsersRepository;
import com.vitalioleksenko.csp.security.CustomUserDetails;
import com.vitalioleksenko.csp.security.Role;
import com.vitalioleksenko.csp.services.ActivitiesLogsService;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UsersService usersService;
    private final UsersRepository usersRepository;
    private final AppMapper mapper;
    private final ActivitiesLogsService activitiesLogsService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, UsersService usersService, UsersRepository usersRepository, @Qualifier("appMapperImpl") AppMapper mapper, ActivitiesLogsService activitiesLogsService) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.usersService = usersService;
        this.usersRepository = usersRepository;
        this.mapper = mapper;
        this.activitiesLogsService = activitiesLogsService;
    }

    @PostMapping("/login")
    public ResponseEntity<HttpStatus>  login(@RequestBody AuthenticationRequest authenticationRequest,
                                             HttpServletRequest request) {


        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        HttpSession session = request.getSession(true);
        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

        activitiesLogsService.log(
                "USER_LOGGED_IN",
                "Logged in"
        );

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/register")
        public ResponseEntity<HttpStatus> register(@RequestBody @Valid RegisterRequest registerRequest,
                                               BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            throw new BadRequestException(
                    ErrorBuilder.fromBindingErrors(bindingResult)
            );
        }
        User user = User.builder()
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .passwordHash(passwordEncoder.encode(registerRequest.getPassword()))
                .role(Role.ROLE_USER)
                .build();

        usersRepository.save(user);
        activitiesLogsService.log(
                "USER_REGISTERED",
                "Registered user with ID: " + user.getUserId()
        );

        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @PostMapping("/logout")
    public ResponseEntity<HttpStatus> logout(HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof CustomUserDetails) {
            activitiesLogsService.log(
                    "USER_LOGGED_OUT",
                    "Logged out"
            );
        }

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
}
