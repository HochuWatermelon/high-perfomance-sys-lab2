package com.company.workservice.model.mapper;

import com.company.workservice.model.dto.worker.WorkerDto;
import com.company.workservice.model.dto.worker.WorkerPostDto;
import com.company.workservice.model.entity.WorkerEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface WorkerMapper {

    WorkerEntity workerPostDtoToEntity(WorkerPostDto workerPostDto, String fullName);

    WorkerDto workerEntityToWorkerDto(WorkerEntity workerEntity);
}
