package com.company.workservice.model.mapper;


import com.company.workservice.model.dto.profession.ProfessionPostDto;
import com.company.workservice.model.entity.ProfessionEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProfessionMapper {
    ProfessionEntity professionPostDtoToEntity(ProfessionPostDto professionPostDto);
}
