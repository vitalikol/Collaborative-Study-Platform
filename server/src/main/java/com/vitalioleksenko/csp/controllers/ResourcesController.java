package com.vitalioleksenko.csp.controllers;

import com.vitalioleksenko.csp.dto.resource.*;
import com.vitalioleksenko.csp.models.Resource;
import com.vitalioleksenko.csp.services.ResourcesService;
import com.vitalioleksenko.csp.util.exceptions.BadRequestException;
import com.vitalioleksenko.csp.util.exceptions.ErrorBuilder;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;

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

    @GetMapping("")
    public Page<ResourceDetailedDTO> readAll(@RequestParam(name = "taskId", required = false) Integer taskId,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "20") int size){
        return resourcesService.findAll(taskId, page, size);
    }

    @GetMapping("/{id}")
    public ResourceDetailedDTO readOne(@PathVariable("id") int id){
        return resourcesService.findById(id);
    }

    @PatchMapping("/{id}")
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
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id){
        resourcesService.remove(id);

        return ResponseEntity.ok(HttpStatus.OK);
    }


    @PostMapping(value ="/{id}/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadFile(
            @PathVariable("id") int id,
            @RequestParam("file") MultipartFile file
    ) {
        try {
            resourcesService.uploadFile(file, id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/{id}/file")
    public ResponseEntity<?> downloadFile(@PathVariable("id") int id) {
        try {
            return resourcesService.getFile(id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

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
