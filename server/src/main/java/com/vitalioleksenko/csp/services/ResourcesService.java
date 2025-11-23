package com.vitalioleksenko.csp.services;

import com.vitalioleksenko.csp.dto.resource.ResourceCreateDTO;
import com.vitalioleksenko.csp.dto.resource.ResourceDetailedDTO;
import com.vitalioleksenko.csp.dto.resource.ResourcePartialDTO;
import com.vitalioleksenko.csp.dto.resource.ResourceUpdateDTO;
import com.vitalioleksenko.csp.models.Resource;
import com.vitalioleksenko.csp.repositories.ResourcesRepository;
import com.vitalioleksenko.csp.util.AppMapper;
import com.vitalioleksenko.csp.util.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
public class ResourcesService {
    private final ResourcesRepository resourcesRepository;
    private final AppMapper mapper;
    private final ActivitiesLogsService activitiesLogsService;
    private final Path root = Paths.get("../db/files");

    @Autowired
    public ResourcesService(ResourcesRepository resourcesRepository, @Qualifier("appMapperImpl") AppMapper mapper, ActivitiesLogsService activitiesLogsService) {
        this.resourcesRepository = resourcesRepository;
        this.mapper = mapper;
        this.activitiesLogsService = activitiesLogsService;
    }

    @Transactional
    public void save(ResourceCreateDTO dto){
        Resource resource = mapper.toResource(dto);
        resourcesRepository.save(resource);
        activitiesLogsService.log(
                "RESOURCE_CREATED",
                "Created resource with ID: " + resource.getResourceId()
        );
    }

    public Page<ResourcePartialDTO> findAll(Integer groupId, int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<Resource> result;

        if (groupId != null) {
            result = resourcesRepository.findByGroup_GroupId(groupId, pageable);
        } else {
            result = resourcesRepository.findAll(pageable);
        }

        return result.map(mapper::toResourcePartial);
    }

    public ResourceDetailedDTO findById(int id){
        Resource resource = resourcesRepository.findById(id).orElseThrow(NotFoundException::new);
        return mapper.toResourceDetailed(resource);
    }

    @Transactional
    public void edit(ResourceUpdateDTO dto, int id){
        Resource resource = resourcesRepository.findById(id).orElseThrow(NotFoundException::new);
        mapper.updateResourceFromDto(dto, resource);
        resourcesRepository.save(resource);
        activitiesLogsService.log(
                "RESOURCE_EDITED",
                "Edited resource with ID: " + resource.getResourceId()
        );
    }

    @Transactional
    public boolean remove(int id){
        return resourcesRepository.findById(id).map(resource -> {
            resourcesRepository.delete(resource);
            activitiesLogsService.log(
                    "RESOURCES_DELETED",
                    "Deleted resource with id " + id
            );
            return true;
        }).orElse(false);
    }

    @Transactional
    public void uploadFile(int resourceId, MultipartFile file) throws IOException {
        Resource resource = resourcesRepository.findById(resourceId).orElseThrow(NotFoundException::new); // метод, який ти вже маєш


        resourcesRepository.save(resource);
    }

    public byte[] getFile(int resourceId) throws IOException {
        Resource resource = resourcesRepository.findById(resourceId).orElseThrow(NotFoundException::new);

        if (resource.getPathOrUrl() == null)
            throw new FileNotFoundException("Resource has no file");

        return Files.readAllBytes(Paths.get(resource.getPathOrUrl()));
    }

    public String getFileName(int resourceId) {
        Resource resource = resourcesRepository.findById(resourceId).orElseThrow(NotFoundException::new);

        if (resource.getPathOrUrl() == null)
            throw new RuntimeException("Resource has no file");

        return Paths.get(resource.getPathOrUrl()).getFileName().toString();
    }

    @Transactional
    public void deleteFile(int resourceId) throws IOException {
        Resource resource = resourcesRepository.findById(resourceId).orElseThrow(NotFoundException::new);


        if (resource.getPathOrUrl() != null) {

            boolean deleted = Files.deleteIfExists(Paths.get(resource.getPathOrUrl()));

            resource.setPathOrUrl(null);
            resourcesRepository.save(resource);
        }
    }

}
