package com.vitalioleksenko.csp.services.security;

import com.vitalioleksenko.csp.models.User;
import com.vitalioleksenko.csp.models.dto.security.JwtResponse;
import com.vitalioleksenko.csp.models.dto.security.AuthenticationRequest;
import com.vitalioleksenko.csp.models.dto.security.RegisterRequest;
import com.vitalioleksenko.csp.repositories.UsersRepository;
import com.vitalioleksenko.csp.security.Role;
import com.vitalioleksenko.csp.services.ActivitiesLogsService;
import com.vitalioleksenko.csp.services.UsersService;
import com.vitalioleksenko.csp.util.AppMapper;
import com.vitalioleksenko.csp.util.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UsersService usersService;
    private final UsersRepository usersRepository;
    private final AppMapper mapper;
    private final ActivitiesLogsService activitiesLogsService;
    private final JwtService jwtService;


    @Autowired
    public AuthService(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, UsersService usersService, UsersRepository usersRepository, @Qualifier("appMapperImpl") AppMapper mapper, ActivitiesLogsService activitiesLogsService, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.usersService = usersService;
        this.usersRepository = usersRepository;
        this.mapper = mapper;
        this.activitiesLogsService = activitiesLogsService;
        this.jwtService = jwtService;
    }

    public JwtResponse login(AuthenticationRequest authenticationRequest){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()
                )
        );
        User user = usersRepository.findByEmail(authenticationRequest.getUsername()).orElseThrow(NotFoundException::new);

        String token = jwtService.generateToken(user.getUserId(), user.getEmail());
        activitiesLogsService.log(
                "USER_LOGGED_IN",
                "Logged in"
        );
        return new JwtResponse(token);
    }

    public void register(RegisterRequest registerRequest){
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
    }
}
