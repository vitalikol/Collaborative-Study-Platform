package com.vitalioleksenko.csp.services;

import com.vitalioleksenko.csp.models.User;
import com.vitalioleksenko.csp.repositories.UsersRepository;
import com.vitalioleksenko.csp.util.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UsersService {
    private final UsersRepository usersRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public UsersService(UsersRepository usersRepository, ModelMapper modelMapper) {
        this.usersRepository = usersRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public void save(User user){
        usersRepository.save(user);
    }

    public List<User> findAll(){
        return usersRepository.findAll();
    }

    public User findById(int id){
        return usersRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    @Transactional
    public void edit(User updatedUser,int id){
        User user = usersRepository.findById(id).orElseThrow(NotFoundException::new);
        modelMapper.map(updatedUser, user);
        usersRepository.save(user);
    }

    @Transactional
    public void remove(int id){
        usersRepository.deleteById(id);
    }
}
