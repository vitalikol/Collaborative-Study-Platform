package com.vitalioleksenko.csp.controllers;

import com.vitalioleksenko.csp.dto.resource.ResourceCreateDTO;
import com.vitalioleksenko.csp.dto.resource.ResourceDetailedDTO;
import com.vitalioleksenko.csp.dto.resource.ResourcePartialDTO;
import com.vitalioleksenko.csp.dto.resource.ResourceUpdateDTO;
import com.vitalioleksenko.csp.services.ResourcesService;
import com.vitalioleksenko.csp.util.exceptions.BadRequestException;
import com.vitalioleksenko.csp.util.exceptions.ErrorBuilder;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/file")
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid ResourceCreateDTO dto,
                                             BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new BadRequestException(
                    ErrorBuilder.fromBindingErrors(bindingResult)
            );
        }

        resourcesService.save(dto);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("")
    public Page<ResourcePartialDTO> readAll(@RequestParam(name = "groupId", required = false) Integer groupId,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "20") int size){
        return resourcesService.findAll(groupId, page, size);
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
            resourcesService.uploadFile(id, file);
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
            byte[] data = resourcesService.getFile(id);
            String fileName = resourcesService.getFileName(id);

            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"" + fileName + "\"")
                    .body(data);

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
