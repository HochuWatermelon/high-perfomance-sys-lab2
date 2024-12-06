package com.company.monolitservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.company.monolitservice.model.entity.WorkerProfessionEntity;
import com.company.monolitservice.repository.WorkerProfessionRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkerProfessionService {
    private final WorkerProfessionRepository workerProfessionRepository;

    public WorkerProfessionEntity findWorkerProfessionByWorkerId(UUID workerId) {
        return workerProfessionRepository.findByWorkerId(workerId);
    }

    public void saveWorkerProfession(WorkerProfessionEntity workerProfession) {
        workerProfessionRepository.save(workerProfession);
    }
}
