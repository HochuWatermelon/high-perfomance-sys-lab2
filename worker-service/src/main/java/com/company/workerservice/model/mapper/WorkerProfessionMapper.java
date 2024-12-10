package com.company.workerservice.model.mapper;

import com.company.workerservice.model.dto.profession.WorkerProfessionDto;
import com.company.workerservice.model.entity.WorkerProfessionEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface WorkerProfessionMapper {
    @Mapping(target = "workerId", source = "worker.id")
    @Mapping(target = "professionName", source = "profession.name")
    WorkerProfessionDto workerProfessionEntityToWorkerProfessionDto(WorkerProfessionEntity workerProfessionEntity);
}
