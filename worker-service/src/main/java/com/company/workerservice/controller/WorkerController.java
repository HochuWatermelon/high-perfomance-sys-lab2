package com.company.workerservice.controller;


import com.company.workerservice.model.dto.worker.WorkerDto;
import com.company.workerservice.model.dto.worker.WorkerPostDto;
import com.company.workerservice.service.WorkerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Validated
@RestController
@RequestMapping("/worker")
@RequiredArgsConstructor
public class WorkerController {
    private final WorkerService workerService;
    @PostMapping
    @PreAuthorize("hasAnyAuthority('WORKER')")
    public ResponseEntity<String> createWorker(@Valid @RequestBody WorkerPostDto workerDto) {
        return workerService.createWorker(workerDto);
    }

    @GetMapping("/{workerId}")
    public WorkerDto getWorker(@PathVariable UUID workerId) {
        return workerService.getWorkerDtoByWorkerId(workerId);
    }

    @PatchMapping
    public void saveWorker(@Valid @RequestBody WorkerDto workerDto){
        workerService.saveWorker(workerDto);
    }

}
