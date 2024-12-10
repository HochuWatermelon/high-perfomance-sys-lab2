package com.company.workerservice.model.mapper;


import com.company.workerservice.model.dto.profession.ProfessionDto;
import com.company.workerservice.model.dto.profession.ProfessionPostDto;
import com.company.workerservice.model.entity.ProfessionEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProfessionMapper {
    ProfessionEntity professionPostDtoToEntity(ProfessionPostDto professionPostDto);
    ProfessionDto professionEntityToProfessionDto(ProfessionEntity professionEntity);
}
