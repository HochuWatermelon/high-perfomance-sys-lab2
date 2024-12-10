package com.company.orderservice.controller;

import com.company.orderservice.model.dto.work.WaitingApprovalWorkPutDto;
import com.company.orderservice.model.dto.work.WorkPutDto;
import com.company.orderservice.service.WorkService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Validated
@RestController
@RequestMapping("/work")
@RequiredArgsConstructor
public class WorkController {
    private final WorkService workService;

    @PutMapping("/make-work")
    @PreAuthorize("hasAnyAuthority('WORKER')")
    public ResponseEntity<String> makeWork(@Valid @RequestBody WorkPutDto workPutDto) {
        return workService.makeWork(workPutDto);
    }


    @PutMapping("/send-required-edits/{orderId}")
    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    public ResponseEntity<String> sendRequiredEdits(@PathVariable UUID orderId, @Valid @RequestBody WaitingApprovalWorkPutDto waitingApprovalWorkPutDto){
        return workService.sendRequiredEditsToWaitingApprovalWork(orderId, waitingApprovalWorkPutDto);
    }

    @PutMapping("/accept-order/{orderId}")
    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    public ResponseEntity<String> acceptOrder(@PathVariable UUID orderId){
        return workService.acceptWork(orderId);
    }
}
