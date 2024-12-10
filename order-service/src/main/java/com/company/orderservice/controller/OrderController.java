package com.company.orderservice.controller;


import com.company.orderservice.model.dto.pageable.PageableGetDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.company.orderservice.model.dto.order.OrderPostDto;
import com.company.orderservice.model.dto.order.OrderPutDto;
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

    @PostMapping
    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    public ResponseEntity<String> createOrder(@Valid @RequestBody OrderPostDto orderDto) {
        return orderService.createOrder(orderDto);
    }

    @PutMapping("/{orderId}")
    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    public ResponseEntity<String> updateOrder(@PathVariable UUID orderId, @Valid @RequestBody OrderPutDto orderDto){
        return orderService.updateOrder(orderId, orderDto);
    }

    @GetMapping("/get-new-orders")
    @PreAuthorize("hasAnyAuthority('WORKER')")
    public ResponseEntity<String> getNewOrders(){
        return orderService.getNewOrders();
    }

    @PostMapping("/subscribe/{orderId}")
    @PreAuthorize("hasAnyAuthority('WORKER')")
    public ResponseEntity<String> subscribeToOrder(@PathVariable UUID orderId) {
        return orderService.subscribeToOrder(orderId);
    }

    @GetMapping("/get-waiting-workers/{orderId}")
    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    public ResponseEntity<String> getWaitingWorkers(@PathVariable UUID orderId, @Valid @RequestBody PageableGetDto pageableGetDto){
        if (pageableGetDto == null) {
            pageableGetDto = new PageableGetDto(0, 50);
        }
        return orderService.getWaitingWorkersForOrderByCustomerId(orderId, pageableGetDto);
    }

    @PutMapping("/add-worker-to-order/{orderId}/{workerId}")
    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    public ResponseEntity<String> addWorkerToOrder(@PathVariable(value = "orderId") UUID orderId, @PathVariable(value = "workerId") UUID workerId){
        return orderService.addWorkerToOrder(workerId, orderId);
    }

    @PutMapping("/pay-for-order/{orderId}")
    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    public ResponseEntity<String> payForOrder(@PathVariable UUID orderId){
        return orderService.payForOrder(orderId);
    }
}
