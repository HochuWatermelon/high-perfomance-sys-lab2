package com.company.workerservice.service;

import com.company.workerservice.model.dto.profession.WorkerProfessionDto;
import com.company.workerservice.model.entity.WorkerProfessionEntity;
import com.company.workerservice.model.mapper.WorkerProfessionMapper;
import com.company.workerservice.repository.WorkerProfessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkerProfessionService {
    private final WorkerProfessionRepository workerProfessionRepository;
    private final WorkerProfessionMapper workerProfessionMapper;

    public WorkerProfessionDto getWorkerProfessionByWorkerId(UUID workerId) {
        WorkerProfessionEntity workerProfession = workerProfessionRepository.findByWorkerId(workerId);
        return workerProfessionMapper.workerProfessionEntityToWorkerProfessionDto(workerProfession);
    }

    public void saveWorkerProfession(WorkerProfessionEntity workerProfession) {
        workerProfessionRepository.save(workerProfession);
    }
}
