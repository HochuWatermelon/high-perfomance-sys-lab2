package com.company.orderservice.model.mapper;

import com.company.orderservice.model.dto.work.WorkPutDto;
import com.company.orderservice.model.entity.WorkEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface WorkMapper {
    @Mapping(target = "requiredEdits", ignore = true)
    @Mapping(target = "acceptedByCustomer", ignore = true)
    @Mapping(target = "workObject", source = "work_object")
    WorkEntity workPutDtoToWorkEntity(WorkPutDto workPutDto);

    @Mapping(target = "workObject", source = "work_object")
    void updateEntityFromWorkPutDto(@MappingTarget WorkEntity workEntity, WorkPutDto workPutDto);
}
