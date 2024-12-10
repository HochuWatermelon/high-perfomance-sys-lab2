package com.company.orderservice.service;


import com.company.orderservice.model.dto.pageable.PageableGetDto;
import com.company.orderservice.service.clients.WorkerServiceClient;
import com.company.orderservice.util.SecurityContext;
import com.company.workerservice.model.dto.profession.WorkerProfessionDto;
import com.company.workerservice.model.dto.worker.WorkerDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.company.orderservice.exceptions.MethodNotAllowedException;
import com.company.orderservice.exceptions.NotEnoughRightsException;
import com.company.orderservice.exceptions.OrderNotFoundException;
import com.company.orderservice.model.dto.order.OrderDto;
import com.company.orderservice.model.dto.order.OrderPostDto;
import com.company.orderservice.model.dto.order.OrderPutDto;
import com.company.orderservice.model.entity.OrderEntity;
import com.company.orderservice.model.entity.OrderStatusEntity;
import com.company.orderservice.model.entity.WaitingWorkerEntity;
import com.company.orderservice.model.mapper.OrderMapper;
import com.company.orderservice.repository.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderStatusService orderStatusService;
    private final WaitingWorkerService waitingWorkerService;
    private final WorkerServiceClient workerServiceClient;


    @Transactional
    public ResponseEntity<String> createOrder(OrderPostDto orderDto){
        UUID customerId = SecurityContext.getAuthorizedUserId();
        String customerName = SecurityContext.getAuthorizedUserName();
        OrderEntity order = orderMapper.orderPostDtoToEntity(orderDto);
        String profession = workerServiceClient.getProfessionByName(orderDto.getProfession_name()).getName();
        OrderStatusEntity orderStatus = orderStatusService.findOrderStatusByCode("NEW");
        order.setCustomerName(customerName);
        order.setCustomerId(customerId);
        order.setProfessionName(profession);
        order.setOrderStatus(orderStatus);
        orderRepository.save(order);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(String.format("Заказ с id = %s был успешно создан", order.getId()));
    }

    public OrderEntity findOrderById(UUID orderId) {
        return orderRepository.findById(orderId).orElseThrow(() ->
                new OrderNotFoundException(String.format("Заказ с id = %s не был найден", orderId)));
    }

    public List<OrderEntity> findOrdersByOrderStatusCodeAndProfessionName(String code, String professionName) {
        return orderRepository.findByOrderStatusCodeAndProfessionName(code, professionName);
    }

    public ResponseEntity<String> updateOrder(UUID orderId, OrderPutDto orderDto) {
        OrderEntity orderEntity = findOrderById(orderId);
        orderMapper.updateEntityFromOrderPutDto(orderEntity, orderDto);
        orderRepository.save(orderEntity);
        return ResponseEntity.status(HttpStatus.OK)
                             .body(String.format("Заказ с id = %s был изменён", orderId));
    }

    @Transactional
    public ResponseEntity<String> getNewOrders() {
        UUID workerId = SecurityContext.getAuthorizedUserId();
        WorkerProfessionDto workerProfessionDto = workerServiceClient.getWorkerProfessionByWorkerId(workerId);
        List<OrderEntity> newOrdersEntity = findOrdersByOrderStatusCodeAndProfessionName("NEW", workerProfessionDto.getProfessionName());
        HttpHeaders headers = new HttpHeaders();
        if (!newOrdersEntity.isEmpty()) {
            String response = newOrdersEntity.stream().map(orderEntity -> {
                        OrderDto orderDto = orderMapper.orderEntityToOrderDto(orderEntity);
                        return orderDto.toString();
                    }
            ).collect(Collectors.joining("\n\n"));
            headers.add("X-Total-Count", String.valueOf(newOrdersEntity.size()));
            return ResponseEntity.status(HttpStatus.OK)
                    .headers(headers)
                    .body(response);
        } else {
            headers.add("X-Total-Count", "0");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .headers(headers)
                    .body("Для вашей профессии в данный момент нет свободных заказов.");
        }
    }

    @Transactional
    public ResponseEntity<String> subscribeToOrder(UUID orderId) {
        UUID workerId = SecurityContext.getAuthorizedUserId();
        OrderEntity order = findOrderById(orderId);
        WorkerProfessionDto workerProfessionDto = workerServiceClient.getWorkerProfessionByWorkerId(workerId);
        if (workerProfessionDto.getProfessionName().equals(order.getProfessionName())) {
            if ("NEW".equals(order.getOrderStatus().getCode())) {
                WaitingWorkerEntity waitingWorker = new WaitingWorkerEntity(workerId, order);
                waitingWorkerService.saveWaitingWorker(waitingWorker);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(String.format("Вы добавлены в список, желающих выполнить заказ с id = %s, ожидайте решения Заказчика.", orderId));
            } else {
                throw new MethodNotAllowedException(String.format("Заказ с id = %s больше недоступен для взятия в исполнение.", orderId));
            }
        } else {
            throw new NotEnoughRightsException(String.format("Для данного заказа требуется работник с профессией name = %s", order.getProfessionName()));
        }
    }

    @Transactional
    public ResponseEntity<String> getWaitingWorkersForOrderByCustomerId(UUID orderId, PageableGetDto pageableGetDto) {
        UUID customerId = SecurityContext.getAuthorizedUserId();
        Pageable pageable = PageRequest.of(pageableGetDto.getPage(), pageableGetDto.getSize());
        List<WaitingWorkerEntity> waitingWorkers = waitingWorkerService.findByOrderCustomerIdAndOrderId(customerId, orderId);
        Page<WaitingWorkerEntity> waitingWorkersPage = waitingWorkerService.findByOrderCustomerIdAndOrderId(customerId, orderId, pageable);
        HttpHeaders headers = new HttpHeaders();
        if (!waitingWorkers.isEmpty()) {
            if (pageable.getPageNumber() >= waitingWorkersPage.getTotalPages()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("На этой странице данных нет, попробуйте меньшее значение page или size.");
            }
            String response = waitingWorkersPage.getContent().stream().map(waitingWorkerEntity -> {
                WorkerDto worker = workerServiceClient.getWorkerByWorkerId(waitingWorkerEntity.getWaitingWorkerId().getWorkerId());
                return worker.toString();
            }).collect(Collectors.joining("\n\n"));
            headers.add("X-Total-Count", String.valueOf(waitingWorkers.size()));
            return ResponseEntity.status(HttpStatus.OK)
                    .headers(headers)
                    .body(response);
        } else {
            headers.add("X-Total-Count", "0");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .headers(headers)
                    .body("По вашему заказу не найдены работники, желающие его выполнить.");
        }
    }

    @Transactional
    public ResponseEntity<String> addWorkerToOrder(UUID workerId, UUID orderId) {
        UUID customerId = SecurityContext.getAuthorizedUserId();
        WaitingWorkerEntity waitingWorker = waitingWorkerService
                .findWaitingWorkerByWorkerIdAndOrderIdAndOrderCustomerId(workerId, orderId, customerId);
        OrderEntity order = waitingWorker.getOrder();
        if ("NEW".equals(order.getOrderStatus().getCode()) && order.getWorkerId() == null) {
            order.setWorkerId(workerId);
            order.setOrderStatus(orderStatusService.findOrderStatusByCode("IN_WORK"));
            orderRepository.save(order);
            waitingWorkerService.deleteWaitingWorker(waitingWorker);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(String.format("Работник с id = %s приступил к работе над заказом с id = %s.", workerId, orderId));
        } else {
            throw new MethodNotAllowedException(String.format("В статус с id = %s нельзя добавлять работника.", orderId));
        }
    }

    @Transactional
    public ResponseEntity<String> payForOrder(UUID orderId) {
        UUID customerId = SecurityContext.getAuthorizedUserId();
        OrderEntity order = findOrderById(orderId);
        if (order.getCustomerId().equals(customerId)) {
            if ("FINISHED".equals(order.getOrderStatus().getCode())) {
                order.setOrderStatus(orderStatusService.findOrderStatusByCode("PAID"));
                orderRepository.save(order);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(String.format("Заказ с id = %s был успешно оплачен.", order.getId()));
            } else {
                throw new MethodNotAllowedException(String.format("Заказ имеет статус code = %s, оплатить заказ можно, если code = FINISHED.", order.getOrderStatus().getCode()));
            }
        }else {
            throw new NotEnoughRightsException(String.format("Заказчик с id = %s не имеет доступа к заказу с id = %s.", customerId, order.getId()));
        }
    }

    public void saveOrder(OrderEntity order) {
        orderRepository.save(order);
    }
}
