package com.vitalioleksenko.csp.controllers;

import com.vitalioleksenko.csp.models.Resource;
import com.vitalioleksenko.csp.services.ResourcesService;
import com.vitalioleksenko.csp.util.BadRequestException;
import com.vitalioleksenko.csp.util.ErrorBuilder;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resource")
public class ResourcesController {
    private final ResourcesService resourcesService;
    private final ModelMapper modelMapper;

    @Autowired
    public ResourcesController(ResourcesService resourcesService, ModelMapper modelMapper) {
        this.resourcesService = resourcesService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("")
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid Resource resource,
                                             BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new BadRequestException(
                    ErrorBuilder.fromBindingErrors(bindingResult)
            );
        }

        resourcesService.save(resource);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("")
    public List<Resource> readAll(){
        return resourcesService.findAll();
    }

    @GetMapping("/{id}")
    public Resource readOne(@PathVariable("id") int id){
        return resourcesService.findById(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable("id") int id,
                                             @RequestBody  @Valid Resource resource,
                                             BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new BadRequestException(
                    ErrorBuilder.fromBindingErrors(bindingResult)
            );
        }

        resourcesService.edit(resource, id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id){
        resourcesService.remove(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
