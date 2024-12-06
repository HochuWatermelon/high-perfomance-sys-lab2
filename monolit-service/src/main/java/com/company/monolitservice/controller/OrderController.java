package com.company.monolitservice.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.company.monolitservice.model.dto.customer.CustomerSelfDto;
import com.company.monolitservice.model.dto.order.OrderPostDto;
import com.company.monolitservice.model.dto.order.OrderPutDto;
import com.company.monolitservice.model.dto.waitingworker.WaitingWorkerPostDto;
import com.company.monolitservice.service.OrderService;
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
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/{customerId}")
    public ResponseEntity<String> createOrder(@Valid @RequestBody OrderPostDto orderDto, @PathVariable UUID customerId) {
        return orderService.createOrder(orderDto, customerId);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<String> updateOrder(@PathVariable UUID orderId, @Valid @RequestBody OrderPutDto orderDto){
        return orderService.updateOrder(orderId, orderDto);
    }

    @GetMapping("/get-new-orders/{workerId}")
    public ResponseEntity<String> getNewOrders(@PathVariable UUID workerId) {
        return orderService.getNewOrders(workerId);
    }

    @PostMapping("/subscribe/{orderId}")
    public ResponseEntity<String> subscribeToOrder(@PathVariable UUID orderId, @Valid @RequestBody WaitingWorkerPostDto waitingWorkerDto) {
        return orderService.subscribeToOrder(waitingWorkerDto, orderId);
    }

    @PutMapping("order/add-worker-to-order/{workerId}")
    public ResponseEntity<String> addWorkerToOrder(@PathVariable UUID workerId, @Valid @RequestBody CustomerSelfDto customerSelfDto){
        return orderService.addWorkerToOrder(workerId, customerSelfDto);
    }

    @PutMapping("/pay-for-order")
    public ResponseEntity<String> payForOrder(@Valid @RequestBody CustomerSelfDto customerSelfDto){
        return orderService.payForOrder(customerSelfDto);
    }
}
