package com.company.authservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.company.authservice.model.entity.WorkerProfessionEntity;
import com.company.authservice.repository.WorkerProfessionRepository;
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
