package com.company.workservice.service;


import com.company.workservice.exceptions.WorkerNotFoundException;
import com.company.workservice.model.dto.worker.WorkerDto;
import com.company.workservice.model.dto.worker.WorkerPostDto;
import com.company.workservice.model.entity.ProfessionEntity;
import com.company.workservice.model.entity.WorkerEntity;
import com.company.workservice.model.entity.WorkerProfessionEntity;
import com.company.workservice.model.mapper.WorkerMapper;
import com.company.workservice.repository.WorkerRepository;
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
        WorkerEntity worker = workerMapper.workerPostDtoToEntity(workerDto);
        workerRepository.save(worker);
        ProfessionEntity profession = professionService.findProfessionByName(workerDto.getProfession_name());
        WorkerProfessionEntity workerProfession = new WorkerProfessionEntity(worker, profession, workerDto.getRank());
        workerProfessionService.saveWorkerProfession(workerProfession);
        String workerName = worker.getSecondName() + " " + worker.getFirstName() + " " + worker.getMiddleName();
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(String.format("Работник с %s был создан с id = %s", workerName, worker.getId()));
    }

    public WorkerEntity findWorkerById(UUID workerId) {
        return workerRepository.findById(workerId).orElseThrow(() ->
                new WorkerNotFoundException(String.format("Работник с id = %s не был найден", workerId)));
    }

    public WorkerDto workerToWorkerDto(WorkerEntity worker){
        return workerMapper.workerEntityToWorkerDto(worker);
    }

    public void saveWorker(WorkerEntity worker){
        workerRepository.save(worker);
    }
}
