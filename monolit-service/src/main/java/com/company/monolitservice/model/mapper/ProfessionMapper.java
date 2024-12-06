package com.company.monolitservice.model.mapper;


import com.company.monolitservice.model.dto.profession.ProfessionPostDto;
import com.company.monolitservice.model.entity.ProfessionEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProfessionMapper {
    ProfessionEntity professionPostDtoToEntity(ProfessionPostDto professionPostDto);
}
