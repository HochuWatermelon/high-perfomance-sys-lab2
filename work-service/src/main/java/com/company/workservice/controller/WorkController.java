package com.company.workservice.controller;

import com.company.workservice.model.dto.customer.CustomerSelfDto;
import com.company.workservice.model.dto.work.WaitingApprovalWorkPutDto;
import com.company.workservice.model.dto.work.WorkPutDto;
import com.company.workservice.service.WorkService;
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

    @PutMapping("/make-work/{workerId}")
    @PreAuthorize("hasAnyAuthority('WORKER')")
    public ResponseEntity<String> makeWork(@PathVariable UUID workerId, @Valid @RequestBody WorkPutDto workPutDto) {
        return workService.makeWork(workPutDto, workerId);
    }


    @PutMapping("/send-required-edits")
    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    public ResponseEntity<String> sendRequiredEdits(@Valid @RequestBody WaitingApprovalWorkPutDto waitingApprovalWorkPutDto){
        return workService.sendRequiredEditsToWaitingApprovalWork(waitingApprovalWorkPutDto);
    }

    @PutMapping("/accept-order")
    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    public ResponseEntity<String> acceptOrder(@Valid @RequestBody CustomerSelfDto customerSelfDto){
        return workService.acceptWork(customerSelfDto);
    }
}
