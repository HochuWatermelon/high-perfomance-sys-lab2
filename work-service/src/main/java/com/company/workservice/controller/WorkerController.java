package com.company.workservice.controller;


import com.company.workservice.model.dto.worker.WorkerPostDto;
import com.company.workservice.service.WorkerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
