package com.vitalioleksenko.csp.services;

import com.vitalioleksenko.csp.dto.resource.*;
import com.vitalioleksenko.csp.models.Resource;
import com.vitalioleksenko.csp.repositories.ResourcesRepository;
import com.vitalioleksenko.csp.util.AppMapper;
import com.vitalioleksenko.csp.util.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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
    public ResourceShortDTO save(ResourceCreateDTO dto){
        Resource resource = mapper.toResource(dto);
        resourcesRepository.save(resource);
        activitiesLogsService.log(
                "RESOURCE_CREATED",
                "Created resource with ID: " + resource.getResourceId()
        );
        return mapper.toResourceShort(resource);
    }

    public Page<ResourceDetailedDTO> findAll(Integer taskId, int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<Resource> result;

        if (taskId != null) {
            result = resourcesRepository.findByTask_TaskId(taskId, pageable);
        } else {
            result = resourcesRepository.findAll(pageable);
        }

        return result.map(mapper::toResourceDetailed);
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
    public void uploadFile(MultipartFile file, int id) throws IOException {
        Resource resource = resourcesRepository.findById(id).orElseThrow(NotFoundException::new);

        Files.createDirectories(root.resolve(String.valueOf(resource.getResourceId())));

        String original = file.getOriginalFilename();
        String fileName = System.currentTimeMillis() + "_" + original;

        Path dest = root.resolve(resource.getResourceId() + "/" + fileName);
        Files.copy(file.getInputStream(), dest, StandardCopyOption.REPLACE_EXISTING);

        resource.setPathOrUrl(dest.toString());
        resource.setUploadedAt(LocalDateTime.now());
        resourcesRepository.save(resource);
    }

    public ResponseEntity<?> getFile(int resourceId) throws IOException {
        Resource resource = resourcesRepository.findById(resourceId)
                .orElseThrow(NotFoundException::new);

        if (resource.getPathOrUrl() == null) {
            throw new FileNotFoundException("Resource has no file");
        }

        Path path = Paths.get(resource.getPathOrUrl());
        File file = path.toFile();

        String mimeType = Files.probeContentType(path);
        if (mimeType == null) {
            mimeType = "application/octet-stream"; // fallback
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(mimeType));
        headers.setContentLength(file.length());
        headers.setContentDisposition(
                ContentDisposition.attachment()
                        .filename(file.getName())
                        .build()
        );

        return new ResponseEntity<>(
                new FileSystemResource(file),
                headers,
                HttpStatus.OK
        );
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
