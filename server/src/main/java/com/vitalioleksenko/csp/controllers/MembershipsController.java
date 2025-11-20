package com.vitalioleksenko.csp.controllers;

import com.vitalioleksenko.csp.dto.membership.MembershipCreateDTO;
import com.vitalioleksenko.csp.dto.membership.MembershipUpdateDTO;
import com.vitalioleksenko.csp.services.MembershipsService;
import com.vitalioleksenko.csp.util.exceptions.BadRequestException;
import com.vitalioleksenko.csp.util.exceptions.ErrorBuilder;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/membership")
public class MembershipsController {
    private final MembershipsService membershipsService;

    @Autowired
    public MembershipsController(MembershipsService membershipsService) {
        this.membershipsService = membershipsService;
    }

    @PostMapping("")
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid MembershipCreateDTO dto,
                                             BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new BadRequestException(
                    ErrorBuilder.fromBindingErrors(bindingResult)
            );
        }

        membershipsService.save(dto);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable("id") int id,
                                             @RequestBody  @Valid MembershipUpdateDTO dto,
                                             BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new BadRequestException(
                    ErrorBuilder.fromBindingErrors(bindingResult)
            );
        }

        membershipsService.edit(dto, id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id){
        membershipsService.remove(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
