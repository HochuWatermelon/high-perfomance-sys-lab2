package com.company.monolitservice.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.company.monolitservice.model.dto.orderstatus.OrderStatusPostDto;
import com.company.monolitservice.service.OrderStatusService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/status")
@RequiredArgsConstructor
public class OrderStatusController {

    private final OrderStatusService orderStatusService;

    @PostMapping
    public ResponseEntity<String> createOrderStatus(@Valid @RequestBody OrderStatusPostDto orderDto) {
        return orderStatusService.createOrderStatus(orderDto);
    }

}
