package com.vitalioleksenko.csp.controllers;

import com.vitalioleksenko.csp.models.dto.resource.ResourceCreateDTO;
import com.vitalioleksenko.csp.models.dto.resource.ResourceDetailedDTO;
import com.vitalioleksenko.csp.models.dto.resource.ResourceShortDTO;
import com.vitalioleksenko.csp.models.dto.resource.ResourceUpdateDTO;
import com.vitalioleksenko.csp.services.ResourcesService;
import com.vitalioleksenko.csp.util.exceptions.BadRequestException;
import com.vitalioleksenko.csp.util.exceptions.ErrorBuilder;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/resource")
public class ResourcesController {
    private final ResourcesService resourcesService;

    @Autowired
    public ResourcesController(ResourcesService resourcesService) {
        this.resourcesService = resourcesService;
    }

    @PostMapping("")
    public ResponseEntity<ResourceShortDTO> create(
            @RequestBody @Valid ResourceCreateDTO dto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new BadRequestException(
                    ErrorBuilder.fromBindingErrors(bindingResult)
            );
        }

        ResourceShortDTO shortDto = resourcesService.save(dto);

        return ResponseEntity.ok(shortDto);
    }

    @PreAuthorize("hasRole('ADMIN') or @accessService.isMemberOfGroupByTaskId(#taskId)")
    @GetMapping("")
    public Page<ResourceDetailedDTO> readAll(@RequestParam(name = "taskId", required = false) Integer taskId,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "20") int size){
        return resourcesService.findAll(taskId, page, size);
    }

    @PreAuthorize("hasRole('ADMIN') or @accessService.isMemberOfGroupByResourceId(#id)")
    @GetMapping("/{id}")
    public ResourceDetailedDTO readOne(@PathVariable("id") int id){
        return resourcesService.findById(id);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @accessService.isMemberOfGroupByResourceId(#id)")
    public ResponseEntity<HttpStatus> update(@PathVariable("id") int id,
                                             @RequestBody  @Valid ResourceUpdateDTO dto,
                                             BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new BadRequestException(
                    ErrorBuilder.fromBindingErrors(bindingResult)
            );
        }

        resourcesService.edit(dto, id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @accessService.isMemberOfGroupByResourceId(#id)")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id){
        resourcesService.remove(id);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or @accessService.isMemberOfGroupByResourceId(#id)")
    @PostMapping(value ="/{id}/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadFile(
            @PathVariable("id") int id,
            @RequestParam("file") MultipartFile file) {
        try {
            resourcesService.uploadFile(file, id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN') or @accessService.isMemberOfGroupByResourceId(#id)")
    @GetMapping("/{id}/file")
    public ResponseEntity<?> downloadFile(@PathVariable("id") int id) {
        try {
            return resourcesService.getFile(id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN') or @accessService.isMemberOfGroupByResourceId(#id)")
    @DeleteMapping("/{id}/file")
    public ResponseEntity<?> deleteFile(@PathVariable("id") int id) {
        try {
            resourcesService.deleteFile(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
