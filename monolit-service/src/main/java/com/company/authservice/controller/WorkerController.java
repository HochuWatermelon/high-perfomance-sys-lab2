package com.company.authservice.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.company.authservice.model.dto.worker.WorkerPostDto;
import com.company.authservice.model.dto.worker.WorkerPutDto;
import com.company.authservice.service.WorkerService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    public ResponseEntity<String> createWorker(@Valid @RequestBody WorkerPostDto workerDto) {
        return workerService.createWorker(workerDto);
    }

    @PutMapping("/{workerId}")
    public ResponseEntity<String> updateWorker(@PathVariable UUID workerId, @Valid @RequestBody WorkerPutDto workerDto) {
        return workerService.updateWorker(workerId, workerDto);
    }

}
