package com.company.monolitservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.company.monolitservice.model.dto.customer.CustomerSelfDto;
import com.company.monolitservice.model.dto.pageable.PageableGetDto;
import com.company.monolitservice.model.dto.work.WaitingApprovalWorkPutDto;
import com.company.monolitservice.model.dto.work.WorkPutDto;
import com.company.monolitservice.service.WorkService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/work")
@RequiredArgsConstructor
public class WorkController {
    private final WorkService workService;

    @PutMapping("/make-work/{workerId}")
    public ResponseEntity<String> makeWork(@PathVariable UUID workerId, @Valid @RequestBody WorkPutDto workPutDto) {
        return workService.makeWork(workPutDto, workerId);
    }

    @GetMapping("/get-works/{workerId}")
    public ResponseEntity<String> getWorks(@PathVariable UUID workerId, @RequestBody(required = false) Optional<PageableGetDto> pageableDto) {
        PageableGetDto pageableGetDto = pageableDto.orElse(new PageableGetDto(0, 50));
        if (pageableGetDto.getSize() > 50)
            pageableGetDto.setSize(50);
        return workService.getWorkerWorks(workerId, pageableGetDto);
    }

    @PutMapping("/send-required-edits")
    public ResponseEntity<String> sendRequiredEdits(@Valid @RequestBody WaitingApprovalWorkPutDto waitingApprovalWorkPutDto){
        return workService.sendRequiredEditsToWaitingApprovalWork(waitingApprovalWorkPutDto);
    }

    @PutMapping("/accept-order")
    public ResponseEntity<String> acceptOrder(@Valid @RequestBody CustomerSelfDto customerSelfDto){
        return workService.acceptWork(customerSelfDto);
    }
}
