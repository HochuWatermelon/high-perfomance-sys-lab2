package com.company.orderservice.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.company.orderservice.exceptions.OrderStatusNotFoundException;
import com.company.orderservice.exceptions.UniqueFieldAlreadyExistException;
import com.company.orderservice.model.dto.orderstatus.OrderStatusPostDto;
import com.company.orderservice.model.entity.OrderStatusEntity;
import com.company.orderservice.model.mapper.OrderStatusMapper;
import com.company.orderservice.repository.OrderStatusRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderStatusService {

    private final OrderStatusRepository orderStatusRepository;
    private final OrderStatusMapper orderStatusMapper;

    public ResponseEntity<String> createOrderStatus(OrderStatusPostDto orderDto) {
        orderDto.setCode(orderDto.getCode().toUpperCase());
        OrderStatusEntity orderStatusEntity = orderStatusMapper.orderStatusPostDtoToOrderStatusEntity(orderDto);
        if (orderStatusRepository.existsByCode(orderStatusEntity.getCode())) {
            throw new UniqueFieldAlreadyExistException(String.format("Заказ с code = %s уже существует.", orderStatusEntity.getCode()));
        } else {
            orderStatusRepository.save(orderStatusEntity);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(String.format("Статус заказа с code = %s был создан.", orderStatusEntity.getCode()));
        }
    }

    public OrderStatusEntity findOrderStatusByCode(String code) {
        return orderStatusRepository.findByCode(code.toUpperCase()).orElseThrow(() ->
                new OrderStatusNotFoundException(String.format("Статус заказа с code = %s не был найден", code)));
    }
}
