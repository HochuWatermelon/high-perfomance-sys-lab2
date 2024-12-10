package com.company.workerservice.service;


import com.company.workerservice.exceptions.ProfessionNotFoundException;
import com.company.workerservice.exceptions.UniqueFieldAlreadyExistException;
import com.company.workerservice.model.dto.profession.ProfessionDto;
import com.company.workerservice.model.dto.profession.ProfessionPostDto;
import com.company.workerservice.model.entity.ProfessionEntity;
import com.company.workerservice.model.mapper.ProfessionMapper;
import com.company.workerservice.repository.ProfessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


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

    public ProfessionDto getProfessionByName(String name) {
        ProfessionEntity profession = professionRepository.findByNameIgnoreCase(name).orElseThrow(() ->
                new ProfessionNotFoundException(String.format("Профессия с name = %s не была найдена", name)));
        return professionMapper.professionEntityToProfessionDto(profession);
    }

}
