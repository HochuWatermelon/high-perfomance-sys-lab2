package com.company.orderservice.controller;


import com.company.orderservice.configurations.security.SecurityConfiguration;
import com.company.orderservice.model.dto.pageable.PageableGetDto;
import com.company.orderservice.model.dto.waitingworker.WaitingWorkerGetDto;
import com.company.orderservice.util.SecurityContext;
import com.company.workservice.util.ReactiveSecurityContext;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.company.orderservice.model.dto.customer.CustomerSelfDto;
import com.company.orderservice.model.dto.order.OrderPostDto;
import com.company.orderservice.model.dto.order.OrderPutDto;
import com.company.orderservice.model.dto.waitingworker.WaitingWorkerPostDto;
import com.company.orderservice.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/{customerId}")
    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    public ResponseEntity<String> createOrder(@Valid @RequestBody OrderPostDto orderDto, @PathVariable UUID customerId) {
        return orderService.createOrder(orderDto, customerId);
    }

    @PutMapping("/{orderId}")
    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    public ResponseEntity<String> updateOrder(@PathVariable UUID orderId, @Valid @RequestBody OrderPutDto orderDto){
        return orderService.updateOrder(orderId, orderDto);
    }

    @GetMapping("/get-new-orders/{workerId}")
    @PreAuthorize("hasAnyAuthority('WORKER')")
    public ResponseEntity<String> getNewOrders(@PathVariable UUID workerId) {
        return orderService.getNewOrders(workerId);
    }

    @PostMapping("/subscribe/{orderId}")
    @PreAuthorize("hasAnyAuthority('WORKER')")
    public ResponseEntity<String> subscribeToOrder(@PathVariable UUID orderId, @Valid @RequestBody WaitingWorkerPostDto waitingWorkerDto) {
        return orderService.subscribeToOrder(waitingWorkerDto, orderId);
    }

    @GetMapping("/get-waiting-workers/{orderId}")
    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    public ResponseEntity<String> getWaitingWorkers(@PathVariable UUID orderId, @Valid @RequestBody PageableGetDto pageableGetDto){
        if (pageableGetDto == null) {
            pageableGetDto = new PageableGetDto(0, 50);
        }
        UUID customerId = SecurityContext.getAuthorizedUserId();
        return orderService.getWaitingWorkersForOrderByCustomerId(orderId, customerId, pageableGetDto);
    }

    @PutMapping("/add-worker-to-order/{workerId}")
    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    public ResponseEntity<String> addWorkerToOrder(@PathVariable UUID workerId, @Valid @RequestBody CustomerSelfDto customerSelfDto){
        return orderService.addWorkerToOrder(workerId, customerSelfDto);
    }

    @PutMapping("/pay-for-order/{orderId}")
    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    public ResponseEntity<String> payForOrder(@PathVariable UUID orderId){
        UUID customerId = SecurityContext.getAuthorizedUserId();
        return orderService.payForOrder(orderId, customerId);
    }
}
