package com.vitalioleksenko.csp.services;

import com.vitalioleksenko.csp.dto.user.UserCreateDTO;
import com.vitalioleksenko.csp.dto.user.UserDetailedDTO;
import com.vitalioleksenko.csp.dto.user.UserPartialDTO;
import com.vitalioleksenko.csp.dto.user.UserUpdateDTO;
import com.vitalioleksenko.csp.models.User;
import com.vitalioleksenko.csp.repositories.UsersRepository;
import com.vitalioleksenko.csp.security.Role;
import com.vitalioleksenko.csp.util.AppMapper;
import com.vitalioleksenko.csp.util.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UsersService {
    private final UsersRepository usersRepository;
    private final AppMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final ActivitiesLogsService activitiesLogsService;

    @Autowired
    public UsersService(UsersRepository usersRepository, @Qualifier("appMapperImpl") AppMapper appMapper, PasswordEncoder passwordEncoder, ActivitiesLogsService activitiesLogsService) {
        this.usersRepository = usersRepository;
        this.mapper = appMapper;
        this.passwordEncoder = passwordEncoder;
        this.activitiesLogsService = activitiesLogsService;
    }

    @Transactional
    public void save(UserCreateDTO dto){
        String passwordHash = passwordEncoder.encode(dto.getPassword());
        User user = mapper.toUser(dto);
        user.setPasswordHash(passwordHash);
        if (user.getRole() == null) user.setRole(Role.ROLE_USER);
        usersRepository.save(user);
        activitiesLogsService.log(
                "USER_CREATED",
                "Created user with ID: " + user.getUserId()
        );
    }

    public Page<UserPartialDTO> getUsers(int page, int size, String search){
        Pageable pageable = PageRequest.of(page, size);
        Page<User> pageResult;

        if (search == null || search.isBlank()) {
            pageResult = usersRepository.findAll(pageable);
        } else {
            pageResult = usersRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                    search, search, pageable
            );
        }

        return pageResult.map(mapper::toUserPartial);
    }

    public UserDetailedDTO findById(int id){
        User user = usersRepository.findById(id).orElseThrow(NotFoundException::new);
        return mapper.toUserDetailed(user);
    }

    @Transactional
    public void edit(UserUpdateDTO updatedUser, int id){
        String passwordHash = passwordEncoder.encode(updatedUser.getPassword());
        User user = usersRepository.findById(id).orElseThrow(NotFoundException::new);
        mapper.updateUserFromDto(updatedUser, user);
        user.setPasswordHash(passwordHash);
        usersRepository.save(user);
        activitiesLogsService.log(
                "USER_EDITED",
                "Edited user with ID: " + user.getUserId()
        );
    }

    @Transactional
    public boolean remove(int id){
        return usersRepository.findById(id).map(user -> {
            usersRepository.delete(user);
            activitiesLogsService.log(
                    "USER_DELETED",
                    "Deleted user with id " + id
            );
            return true;
        }).orElse(false);
    }
}
