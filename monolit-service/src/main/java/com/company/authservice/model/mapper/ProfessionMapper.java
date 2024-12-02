package com.company.authservice.model.mapper;


import com.company.authservice.model.dto.profession.ProfessionPostDto;
import com.company.authservice.model.entity.ProfessionEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProfessionMapper {
    ProfessionEntity professionPostDtoToEntity(ProfessionPostDto professionPostDto);
}
