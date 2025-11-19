package com.vitalioleksenko.csp.services;

import com.vitalioleksenko.csp.dto.user.UserCreateDTO;
import com.vitalioleksenko.csp.dto.user.UserDetailedDTO;
import com.vitalioleksenko.csp.dto.user.UserPartialDTO;
import com.vitalioleksenko.csp.dto.user.UserUpdateDTO;
import com.vitalioleksenko.csp.models.User;
import com.vitalioleksenko.csp.repositories.UsersRepository;
import com.vitalioleksenko.csp.util.AppMapper;
import com.vitalioleksenko.csp.util.NotFoundException;
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

    @Autowired
    public UsersService(UsersRepository usersRepository, @Qualifier("appMapperImpl") AppMapper appMapper, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.mapper = appMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void save(UserCreateDTO dto){
        User user = mapper.toUser(dto);
        String passwordHash = passwordEncoder.encode(user.getPasswordHash());
        user.setPasswordHash(passwordHash);
        usersRepository.save(user);
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
        User user = usersRepository.findById(id).orElseThrow(NotFoundException::new);
        mapper.updateUserFromDto(updatedUser, user);
        usersRepository.save(user);
    }

    @Transactional
    public void remove(int id){
        usersRepository.deleteById(id);
    }
}
