package com.company.monolitservice.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.company.monolitservice.exceptions.MethodNotAllowedException;
import com.company.monolitservice.exceptions.NotEnoughRightsException;
import com.company.monolitservice.exceptions.OrderNotFoundException;
import com.company.monolitservice.model.dto.customer.CustomerSelfDto;
import com.company.monolitservice.model.dto.order.OrderDto;
import com.company.monolitservice.model.dto.order.OrderPostDto;
import com.company.monolitservice.model.dto.order.OrderPutDto;
import com.company.monolitservice.model.dto.waitingworker.WaitingWorkerPostDto;
import com.company.monolitservice.model.entity.CustomerEntity;
import com.company.monolitservice.model.entity.OrderEntity;
import com.company.monolitservice.model.entity.OrderStatusEntity;
import com.company.monolitservice.model.entity.ProfessionEntity;
import com.company.monolitservice.model.entity.WaitingWorkerEntity;
import com.company.monolitservice.model.entity.WorkerEntity;
import com.company.monolitservice.model.entity.WorkerProfessionEntity;
import com.company.monolitservice.model.mapper.OrderMapper;
import com.company.monolitservice.repository.OrderRepository;
import org.springframework.data.domain.Page;
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
    private final CustomerService customerService;
    private final ProfessionService professionService;
    private final WaitingWorkerService waitingWorkerService;
    private final WorkerService workerService;
    private final WorkerProfessionService workerProfessionService;


    @Transactional
    public ResponseEntity<String> createOrder(OrderPostDto orderDto, UUID customerId){
        OrderEntity order = orderMapper.orderPostDtoToEntity(orderDto);
        CustomerEntity customer = customerService.findCustomerById(customerId);
        ProfessionEntity profession = professionService.findProfessionByName(orderDto.getProfession_name());
        OrderStatusEntity orderStatus = orderStatusService.findOrderStatusByCode("NEW");
        order.setCustomer(customer);
        order.setProfession(profession);
        order.setOrderStatus(orderStatus);
        orderRepository.save(order);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(String.format("Заказ с id = %s был успешно создан", order.getId()));
    }

    public OrderEntity findOrderById(UUID orderId) {
        return orderRepository.findById(orderId).orElseThrow(() ->
                new OrderNotFoundException(String.format("Заказ с id = %s не был найден", orderId)));
    }

    public List<OrderEntity> findOrdersByOrderStatusCodeAndProfessionId(String code, UUID professionId) {
        return orderRepository.findByOrderStatusCodeAndProfessionId(code, professionId);
    }

    public ResponseEntity<String> updateOrder(UUID orderId, OrderPutDto orderDto) {
        OrderEntity orderEntity = findOrderById(orderId);
        orderMapper.updateEntityFromOrderPutDto(orderEntity, orderDto);
        orderRepository.save(orderEntity);
        return ResponseEntity.status(HttpStatus.OK)
                             .body(String.format("Заказ с id = %s был изменён", orderId));
    }

    @Transactional
    public ResponseEntity<String> getNewOrders(UUID workerId) {
        WorkerProfessionEntity workerProfession = workerProfessionService.findWorkerProfessionByWorkerId(workerId);
        List<OrderEntity> newOrdersEntity = findOrdersByOrderStatusCodeAndProfessionId("NEW", workerProfession.getProfession().getId());
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
    public ResponseEntity<String> subscribeToOrder(WaitingWorkerPostDto waitingWorkerDto, UUID orderId) {
        WorkerEntity worker = workerService.findWorkerById(waitingWorkerDto.getWorker_id());
        OrderEntity order = findOrderById(orderId);
        WorkerProfessionEntity workerProfession = workerProfessionService.findWorkerProfessionByWorkerId(worker.getId());
        if (workerProfession.getProfession().getId().equals(order.getProfession().getId())) {
            if ("NEW".equals(order.getOrderStatus().getCode())) {
                WaitingWorkerEntity waitingWorker = new WaitingWorkerEntity(worker, order);
                waitingWorkerService.saveWaitingWorker(waitingWorker);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(String.format("Вы добавлены в список, желающих выполнить заказ с id = %s, ожидайте решения Заказчика.", orderId));
            } else {
                throw new MethodNotAllowedException(String.format("Заказ с id = %s больше недоступен для взятия в исполнение.", orderId));
            }
        } else {
            throw new NotEnoughRightsException(String.format("Для данного заказа требуется работник с профессией id = %s", order.getProfession().getId()));
        }
    }

    @Transactional
    public ResponseEntity<String> addWorkerToOrder(UUID workerId, CustomerSelfDto customerSelfDto) {
        UUID orderId = customerSelfDto.getOrder_id();
        UUID customerId = customerSelfDto.getCustomer_id();
        WaitingWorkerEntity waitingWorker = waitingWorkerService
                .findWaitingWorkerByWorkerIdAndOrderIdAndOrderCustomerId(workerId, orderId, customerId);
        OrderEntity order = waitingWorker.getOrder();
        WorkerEntity worker = waitingWorker.getWorker();
        if ("NEW".equals(order.getOrderStatus().getCode()) && order.getWorker() == null) {
            order.setWorker(worker);
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
    public ResponseEntity<String> payForOrder(CustomerSelfDto customerSelfDto) {
        OrderEntity order = findOrderById(customerSelfDto.getOrder_id());
        CustomerEntity customer = customerService.findCustomerById(customerSelfDto.getCustomer_id());
        if (order.getCustomer().getId().equals(customerSelfDto.getCustomer_id())) {
            if ("FINISHED".equals(order.getOrderStatus().getCode())) {
                order.setOrderStatus(orderStatusService.findOrderStatusByCode("PAID"));
                orderRepository.save(order);
                customer.setNumberOfPaidOrders(customer.getNumberOfPaidOrders() + 1);
                customerService.saveCustomer(customer);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(String.format("Заказ с id = %s был успешно оплачен.", order.getId()));
            } else {
                throw new MethodNotAllowedException(String.format("Заказ имеет статус code = %s, оплатить заказ можно, если code = FINISHED.", order.getOrderStatus().getCode()));
            }
        }else {
            throw new NotEnoughRightsException(String.format("Заказчик с id = %s не имеет доступа к заказу с id = %s.", customerSelfDto.getCustomer_id(), order.getId()));
        }
    }


    public Page<OrderEntity> findOrderWithWorkByWorkerId(UUID workerId, Pageable pageable) {
        return orderRepository.findByWorkerIdAndWorkNotNull(workerId, pageable);
    }

    public List<OrderEntity> findOrderWithWorkByWorkerId(UUID workerId) {
        return orderRepository.findByWorkerIdAndWorkNotNull(workerId);
    }

    public void saveOrder(OrderEntity order) {
        orderRepository.save(order);
    }
}
