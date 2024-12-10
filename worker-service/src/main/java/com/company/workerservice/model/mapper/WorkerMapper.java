package com.company.workerservice.model.mapper;

import com.company.workerservice.model.dto.worker.WorkerDto;
import com.company.workerservice.model.dto.worker.WorkerPostDto;
import com.company.workerservice.model.entity.WorkerEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface WorkerMapper {

    WorkerEntity workerPostDtoToEntity(WorkerPostDto workerPostDto, String fullName);

    WorkerDto workerEntityToWorkerDto(WorkerEntity workerEntity);

}
