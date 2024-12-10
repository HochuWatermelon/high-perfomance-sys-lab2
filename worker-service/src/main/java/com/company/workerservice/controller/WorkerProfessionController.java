package com.company.workerservice.controller;

import com.company.workerservice.model.dto.profession.WorkerProfessionDto;
import com.company.workerservice.service.WorkerProfessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Validated
@RestController
@RequestMapping("/worker-profession")
@RequiredArgsConstructor
public class WorkerProfessionController {

    private final WorkerProfessionService workerProfessionService;

    @GetMapping("/{workerId}")
    public WorkerProfessionDto getWorkerProfessionByWorkerId(@PathVariable UUID workerId) {
        return workerProfessionService.getWorkerProfessionByWorkerId(workerId);
    }
}
