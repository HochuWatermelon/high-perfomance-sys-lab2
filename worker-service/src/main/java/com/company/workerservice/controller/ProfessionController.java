package com.company.workerservice.controller;


import com.company.workerservice.model.dto.profession.ProfessionDto;
import com.company.workerservice.model.dto.profession.ProfessionPostDto;
import com.company.workerservice.service.ProfessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Validated
@RestController
@RequestMapping("/profession")
@RequiredArgsConstructor
public class ProfessionController {
    private final ProfessionService professionService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('SUPERVISOR')")
    public ResponseEntity<String> createProfession(@Valid @RequestBody ProfessionPostDto professionDto) {
        return professionService.createProfession(professionDto);
    }

    @GetMapping("/{name}")
    public ProfessionDto getProfessionByName(@PathVariable String name) {
        return professionService.getProfessionByName(name);
    }

}
