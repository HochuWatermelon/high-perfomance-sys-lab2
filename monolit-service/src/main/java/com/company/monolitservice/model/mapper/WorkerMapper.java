package com.company.monolitservice.model.mapper;

import com.company.monolitservice.model.dto.worker.WorkerDto;
import com.company.monolitservice.model.dto.worker.WorkerPostDto;
import com.company.monolitservice.model.dto.worker.WorkerPutDto;
import com.company.monolitservice.model.entity.WorkerEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface WorkerMapper {

    @Mapping(target = "firstName", source = "first_name")
    @Mapping(target = "secondName", source = "second_name")
    @Mapping(target = "middleName", source = "middle_name")
    WorkerEntity workerPostDtoToEntity(WorkerPostDto workerPostDto);

    WorkerDto workerEntityToWorkerDto(WorkerEntity workerEntity);

    @Mapping(target = "firstName", source = "first_name")
    @Mapping(target = "secondName", source = "second_name")
    @Mapping(target = "middleName", source = "middle_name")
    void updateEntityFromWorkerPutDto(@MappingTarget WorkerEntity entity, WorkerPutDto dto);
}
