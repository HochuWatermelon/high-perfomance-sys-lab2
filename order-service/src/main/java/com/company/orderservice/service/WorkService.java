package com.company.orderservice.service;

import com.company.orderservice.exceptions.MethodNotAllowedException;
import com.company.orderservice.exceptions.NotEnoughRightsException;
import com.company.orderservice.model.dto.work.WaitingApprovalWorkPutDto;
import com.company.orderservice.model.dto.work.WorkPutDto;
import com.company.orderservice.model.entity.OrderEntity;
import com.company.orderservice.model.entity.WorkEntity;
import com.company.orderservice.model.mapper.WorkMapper;
import com.company.orderservice.repository.WorkRepository;
import com.company.orderservice.service.clients.WorkerServiceClient;
import com.company.orderservice.util.SecurityContext;
import com.company.workerservice.model.dto.worker.WorkerDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkService {

    private final WorkRepository workRepository;
    private final WorkMapper workMapper;
    private final OrderService orderService;
    private final OrderStatusService orderStatusService;
    private final WaitingWorkerService waitingWorkerService;
    private final WorkerServiceClient workerServiceClient;

    @Transactional
    public ResponseEntity<String> makeWork(WorkPutDto workDto) {
        UUID workerId = SecurityContext.getAuthorizedUserId();
        OrderEntity order = orderService.findOrderById(workDto.getOrder_id());
        if (order.getWorkerId() != null && workerId.equals(order.getWorkerId())) {
            String orderStatusCode = order.getOrderStatus().getCode();
            if ("IN_WORK".equals(orderStatusCode) || "WAITING_APPROVAL".equals(orderStatusCode)) {
                WorkEntity work;
                if (order.getWork() == null)
                    work = workMapper.workPutDtoToWorkEntity(workDto);
                else {
                    work = order.getWork();
                    workMapper.updateEntityFromWorkPutDto(work, workDto);
                }
                workRepository.save(work);
                order.setWork(work);
                if ("IN_WORK".equals(orderStatusCode))
                    order.setOrderStatus(orderStatusService.findOrderStatusByCode("WAITING_APPROVAL"));
                orderService.saveOrder(order);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(String.format("Сделал работу по заказу с id = %s, теперь можно и отдохнуть.", order.getId()));
            } else {
                throw new MethodNotAllowedException(String.format("Заказ находится в статусе %s, вы не можете выполнять работу по нему.", orderStatusCode));
            }
        } else {
            throw new NotEnoughRightsException(String.format("Работник с id = %s не имеет доступа к выполнению заказа c id = %s", workerId, order.getId()));
        }
    }


    @Transactional
    public ResponseEntity<String> sendRequiredEditsToWaitingApprovalWork(UUID orderId, WaitingApprovalWorkPutDto workDto) {
        UUID customerId = SecurityContext.getAuthorizedUserId();
        String requiredEdits = workDto.getRequired_edits();
        OrderEntity order = orderService.findOrderById(orderId);
        if (order.getCustomerId().equals(customerId)) {
            if ("WAITING_APPROVAL".equals(order.getOrderStatus().getCode())) {
                WorkEntity work = order.getWork();
                work.setRequiredEdits(requiredEdits);
                workRepository.save(work);
                order.setOrderStatus(orderStatusService.findOrderStatusByCode("IN_WORK"));
                order.setWork(work);
                orderService.saveOrder(order);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(String.format("В работу по заказу c id = %s было внесено описание необходимых правок.", order.getId()));
            }else {
                throw new MethodNotAllowedException(String.format("Заказ имеет статус code = %s, правки можно вносить, если code = WAITING_APPROVAL.", order.getOrderStatus().getCode()));
            }
        } else {
            throw new NotEnoughRightsException(String.format("Заказчик с id = %s не имеет доступа к заказу с id = %s.", customerId, order.getId()));
        }
    }

    @Transactional
    public ResponseEntity<String> acceptWork(UUID orderId) {
        UUID customerId = SecurityContext.getAuthorizedUserId();
        OrderEntity order = orderService.findOrderById(orderId);
        if (order.getCustomerId().equals(customerId)) {
            if ("WAITING_APPROVAL".equals(order.getOrderStatus().getCode())) {
                WorkEntity work = order.getWork();
                WorkerDto workerDto = workerServiceClient.getWorkerByWorkerId(order.getWorkerId());
                work.setAcceptedByCustomer(true);
                workRepository.save(work);
                order.setWork(work);
                order.setOrderStatus(orderStatusService.findOrderStatusByCode("FINISHED"));
                workerDto.setNumberOfCompletedOrders(workerDto.getNumberOfCompletedOrders() + 1);
                workerServiceClient.saveWorker(workerDto);
                orderService.saveOrder(order);
                waitingWorkerService.deleteWaitingWorkersByOrderId(order.getId());
                return ResponseEntity.status(HttpStatus.OK)
                        .body(String.format("Заказ с id = %s успешно принят.", order.getId()));
            } else {
                throw new MethodNotAllowedException(String.format("Заказ имеет статус code = %s, принять заказ можно, если code = WAITING_APPROVAL.", order.getOrderStatus().getCode()));
            }
        } else {
            throw new NotEnoughRightsException(String.format("Заказчик с id = %s не имеет доступа к заказу с id = %s.", customerId, order.getId()));
        }

    }

}
