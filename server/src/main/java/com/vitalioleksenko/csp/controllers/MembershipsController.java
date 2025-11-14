package com.vitalioleksenko.csp.controllers;

import com.vitalioleksenko.csp.models.Membership;
import com.vitalioleksenko.csp.models.Resource;
import com.vitalioleksenko.csp.services.MembershipsService;
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
@RequestMapping("/api/membership")
public class MembershipsController {
    private final MembershipsService membershipsService;
    private final ModelMapper modelMapper;

    @Autowired
    public MembershipsController(MembershipsService membershipsService, ModelMapper modelMapper) {
        this.membershipsService = membershipsService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("")
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid Membership membership,
                                             BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new BadRequestException(
                    ErrorBuilder.fromBindingErrors(bindingResult)
            );
        }

        membershipsService.save(membership);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("")
    public List<Membership> readAll(){
        return membershipsService.findAll();
    }

    @GetMapping("/{id}")
    public Membership readOne(@PathVariable("id") int id){
        return membershipsService.findById(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable("id") int id,
                                             @RequestBody  @Valid Membership membership,
                                             BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new BadRequestException(
                    ErrorBuilder.fromBindingErrors(bindingResult)
            );
        }

        membershipsService.edit(membership, id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id){
        membershipsService.remove(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
