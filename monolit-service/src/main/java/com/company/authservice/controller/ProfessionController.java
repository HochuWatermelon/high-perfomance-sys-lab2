package com.company.authservice.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.company.authservice.model.dto.profession.ProfessionPostDto;
import com.company.authservice.service.ProfessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
    public ResponseEntity<String> createProfession(@Valid @RequestBody ProfessionPostDto professionDto) {
        return professionService.createProfession(professionDto);
    }

}
