package com.vitalioleksenko.csp.services;

import com.vitalioleksenko.csp.models.Resource;
import com.vitalioleksenko.csp.repositories.ResourcesRepository;
import com.vitalioleksenko.csp.util.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ResourcesService {
    private final ResourcesRepository resourcesRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ResourcesService(ResourcesRepository resourcesRepository, ModelMapper modelMapper) {
        this.resourcesRepository = resourcesRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public void save(Resource resource){
        resourcesRepository.save(resource);
    }

    public List<Resource> findAll(){
        return resourcesRepository.findAll();
    }

    public Resource findById(int id){
        return resourcesRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    @Transactional
    public void edit(Resource updatedResource, int id){
        Resource resource = resourcesRepository.findById(id).orElseThrow(NotFoundException::new);
        modelMapper.map(updatedResource, resource);
        resourcesRepository.save(resource);
    }

    @Transactional
    public void remove(int id){
        resourcesRepository.deleteById(id);
    }
}
