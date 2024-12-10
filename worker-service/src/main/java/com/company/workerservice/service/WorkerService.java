package com.company.workerservice.service;


import com.company.workerservice.exceptions.WorkerNotFoundException;
import com.company.workerservice.model.dto.worker.WorkerDto;
import com.company.workerservice.model.dto.worker.WorkerPostDto;
import com.company.workerservice.model.entity.ProfessionEntity;
import com.company.workerservice.model.entity.WorkerEntity;
import com.company.workerservice.model.entity.WorkerProfessionEntity;
import com.company.workerservice.model.mapper.WorkerMapper;
import com.company.workerservice.repository.WorkerRepository;
import com.company.workerservice.util.SecurityContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class WorkerService {
    private final WorkerRepository workerRepository;
    private final WorkerMapper workerMapper;
    private final ProfessionService professionService;
    private final WorkerProfessionService workerProfessionService;

    @Transactional
    public ResponseEntity<String> createWorker(WorkerPostDto workerDto) {
        String fullName = SecurityContext.getAuthorizedUserName();
        UUID workerId = SecurityContext.getAuthorizedUserId();
        WorkerEntity worker = workerMapper.workerPostDtoToEntity(workerDto, fullName);
        worker.setId(workerId);
        workerRepository.save(worker);
        ProfessionEntity profession = professionService.findProfessionByName(workerDto.getProfession_name());
        WorkerProfessionEntity workerProfession = new WorkerProfessionEntity(worker, profession, workerDto.getRank());
        workerProfessionService.saveWorkerProfession(workerProfession);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(String.format("Работник с %s был создан с id = %s", fullName, worker.getId()));
    }

    public WorkerDto getWorkerDtoByWorkerId(UUID workerID) {
        WorkerEntity worker = findWorkerById(workerID);
        return workerToWorkerDto(worker);
    }

    public WorkerEntity findWorkerById(UUID workerId) {
        return workerRepository.findById(workerId).orElseThrow(() ->
                new WorkerNotFoundException(String.format("Работник с id = %s не был найден", workerId)));
    }

    public WorkerDto workerToWorkerDto(WorkerEntity worker){
        return workerMapper.workerEntityToWorkerDto(worker);
    }

    public void saveWorker(WorkerDto workerDto){
        WorkerEntity worker = findWorkerById(workerDto.getId());
        worker.setNumberOfCompletedOrders(workerDto.getNumberOfCompletedOrders());
        worker.setFullName(workerDto.getFullName());
        workerRepository.save(worker);
    }
}
