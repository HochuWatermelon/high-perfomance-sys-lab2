package com.company.monolitservice.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.company.monolitservice.exceptions.WorkerNotFoundException;
import com.company.monolitservice.model.dto.worker.WorkerDto;
import com.company.monolitservice.model.dto.worker.WorkerPostDto;
import com.company.monolitservice.model.dto.worker.WorkerPutDto;
import com.company.monolitservice.model.entity.FeedbackEntity;
import com.company.monolitservice.model.entity.ProfessionEntity;
import com.company.monolitservice.model.entity.WorkerEntity;
import com.company.monolitservice.model.entity.WorkerProfessionEntity;
import com.company.monolitservice.model.mapper.WorkerMapper;
import com.company.monolitservice.repository.WorkerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Set;
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

    public ResponseEntity<String> updateWorker(UUID workerId, WorkerPutDto workerDto) {
        WorkerEntity workerEntity = findWorkerById(workerId);
        workerMapper.updateEntityFromWorkerPutDto(workerEntity, workerDto);
        workerRepository.save(workerEntity);
        return ResponseEntity.status(HttpStatus.OK)
                             .body(String.format("Работник с id = %s был изменён", workerId));
    }


    public double calculateWorkerRating(WorkerEntity worker) {
        Set<FeedbackEntity> feedbacks = worker.getFeedbacks();
        double sumScore = 0.0;
        for (FeedbackEntity feedbackEntity : feedbacks) {
            sumScore += feedbackEntity.getScore();
        }
        double rating = sumScore / feedbacks.size();
        rating = Math.round(rating * 10.0) / 10.0;
        return rating;
    }

    public WorkerDto workerToWorkerDto(WorkerEntity worker){
        return workerMapper.workerEntityToWorkerDto(worker);
    }

    public void saveWorker(WorkerEntity worker){
        workerRepository.save(worker);
    }
}
