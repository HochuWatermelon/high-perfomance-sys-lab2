package com.company.monolitservice.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.company.monolitservice.model.dto.customer.CustomerPostDto;
import com.company.monolitservice.model.dto.customer.CustomerPutDto;
import com.company.monolitservice.model.dto.pageable.PageableGetDto;
import com.company.monolitservice.model.dto.waitingworker.WaitingWorkerGetDto;
import com.company.monolitservice.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Validated
@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<String> createCustomer(@Valid @RequestBody CustomerPostDto customerDto) {
        return customerService.createCustomer(customerDto);
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<String> update(@PathVariable UUID customerId, @Valid @RequestBody CustomerPutDto customerDto){
        return customerService.updateCustomer(customerId, customerDto);
    }


    @GetMapping("/order/get-waiting-workers")
    public ResponseEntity<String> getWaitingWorkers(@Valid @RequestBody WaitingWorkerGetDto waitingWorkerGetDto){
        if (waitingWorkerGetDto.getPageable_get_dto() == null) {
            waitingWorkerGetDto.setPageable_get_dto(new PageableGetDto(0, 50));
        }
        return customerService.getWaitingWorkersForOrderByCustomerId(waitingWorkerGetDto);
    }
}
