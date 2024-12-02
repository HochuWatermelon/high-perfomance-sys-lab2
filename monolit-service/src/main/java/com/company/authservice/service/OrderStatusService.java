package com.company.authservice.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.company.authservice.exceptions.OrderStatusNotFoundException;
import com.company.authservice.exceptions.UniqueFieldAlreadyExistException;
import com.company.authservice.model.dto.orderstatus.OrderStatusPostDto;
import com.company.authservice.model.entity.OrderStatusEntity;
import com.company.authservice.model.mapper.OrderStatusMapper;
import com.company.authservice.repository.OrderStatusRepository;
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
