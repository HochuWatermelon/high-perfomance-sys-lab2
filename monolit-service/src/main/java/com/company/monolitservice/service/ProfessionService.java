package com.company.monolitservice.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.company.monolitservice.exceptions.ProfessionNotFoundException;
import com.company.monolitservice.exceptions.UniqueFieldAlreadyExistException;
import com.company.monolitservice.model.dto.profession.ProfessionPostDto;
import com.company.monolitservice.model.entity.ProfessionEntity;
import com.company.monolitservice.model.mapper.ProfessionMapper;
import com.company.monolitservice.repository.ProfessionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfessionService {

    private final ProfessionRepository professionRepository;
    private final ProfessionMapper professionMapper;

    public ResponseEntity<String> createProfession(ProfessionPostDto professionDto){
        ProfessionEntity profession = professionMapper.professionPostDtoToEntity(professionDto);
        if (professionRepository.existsByNameIgnoreCase(profession.getName())) {
            throw new UniqueFieldAlreadyExistException(String.format("Профессия с именем %s уже существует.", profession.getName()));
        } else {
            professionRepository.save(profession);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(String.format("Профессия %s c id = %s была создана", profession.getName(), profession.getId()));
        }
    }

    public ProfessionEntity findProfessionByName(String name) {
        return professionRepository.findByNameIgnoreCase(name).orElseThrow(() ->
                new ProfessionNotFoundException(String.format("Профессия с name = %s не была найдена", name)));
    }

    public ProfessionEntity findProfessionById(UUID professionId) {
        return professionRepository.findById(professionId).orElseThrow(() ->
                new ProfessionNotFoundException(String.format("Профессия с id = %s не была найдена", professionId)));
    }


}
