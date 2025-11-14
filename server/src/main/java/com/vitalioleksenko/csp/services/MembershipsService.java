package com.vitalioleksenko.csp.services;

import com.vitalioleksenko.csp.models.Membership;
import com.vitalioleksenko.csp.repositories.MembershipsRepository;
import com.vitalioleksenko.csp.util.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class MembershipsService {
    private final MembershipsRepository membershipRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public MembershipsService(MembershipsRepository membershipRepository, ModelMapper modelMapper) {
        this.membershipRepository = membershipRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public void save(Membership membership){
        membershipRepository.save(membership);
    }

    public List<Membership> findAll(){
        return membershipRepository.findAll();
    }

    public Membership findById(int id){
        return membershipRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    @Transactional
    public void edit(Membership updatedMembership, int id){
        Membership membership = membershipRepository.findById(id).orElseThrow(NotFoundException::new);
        modelMapper.map(updatedMembership, membership);
        membershipRepository.save(membership);
    }

    @Transactional
    public void remove(int id){
        membershipRepository.deleteById(id);
    }
}
