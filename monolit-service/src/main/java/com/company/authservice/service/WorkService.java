package com.company.authservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.company.authservice.exceptions.MethodNotAllowedException;
import com.company.authservice.exceptions.NotEnoughRightsException;
import com.company.authservice.model.dto.customer.CustomerSelfDto;
import com.company.authservice.model.dto.pageable.PageableGetDto;
import com.company.authservice.model.dto.work.WaitingApprovalWorkPutDto;
import com.company.authservice.model.dto.work.WorkDto;
import com.company.authservice.model.dto.work.WorkPutDto;
import com.company.authservice.model.entity.OrderEntity;
import com.company.authservice.model.entity.WorkEntity;
import com.company.authservice.model.entity.WorkerEntity;
import com.company.authservice.model.mapper.WorkMapper;
import com.company.authservice.repository.WorkRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkService {

    private final WorkRepository workRepository;
    private final WorkMapper workMapper;
    private final WorkerService workerService;
    private final OrderService orderService;
    private final OrderStatusService orderStatusService;
    private final WaitingWorkerService waitingWorkerService;

    @Transactional
    public ResponseEntity<String> makeWork(WorkPutDto workDto, UUID workerId) {
        WorkerEntity worker = workerService.findWorkerById(workerId);
        OrderEntity order = orderService.findOrderById(workDto.getOrder_id());
        if (order.getWorker() != null && worker.getId().equals(order.getWorker().getId())) {
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
            throw new NotEnoughRightsException(String.format("Работник с id = %s не имеет доступа к выполнению заказа c id = %s", worker.getId(), order.getId()));
        }
    }

    @Transactional
    public ResponseEntity<String> getWorkerWorks(UUID workerId, PageableGetDto pageableDto) {
        Pageable pageable = PageRequest.of(pageableDto.getPage(), pageableDto.getSize());
        Page<OrderEntity> ordersPage = orderService.findOrderWithWorkByWorkerId(workerId, pageable);
        List<OrderEntity> orderEntities = orderService.findOrderWithWorkByWorkerId(workerId);
        if(!orderEntities.isEmpty()){
            if (pageable.getPageNumber() >= ordersPage.getTotalPages()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("На этой странице данных нет, попробуйте меньшее значение page или size.");
            }
            List<WorkEntity> works = ordersPage.getContent().stream().map(OrderEntity::getWork).toList();
            String response = works.stream().map(workEntity -> {
                WorkDto workDto = workMapper.workEntityToWorkDto(workEntity);
                return workDto.toString();
            }).collect(Collectors.joining("\n\n"));
            return ResponseEntity.status(HttpStatus.OK)
                    .body(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("У вас нет созданных работ.");
        }
    }

    @Transactional
    public ResponseEntity<String> sendRequiredEditsToWaitingApprovalWork(WaitingApprovalWorkPutDto workDto) {
        CustomerSelfDto customerSelfDto = workDto.getCustomer_self_dto();
        String requiredEdits = workDto.getRequired_edits();
        OrderEntity order = orderService.findOrderById(customerSelfDto.getOrder_id());
        if (order.getCustomer().getId().equals(customerSelfDto.getCustomer_id())) {
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
            throw new NotEnoughRightsException(String.format("Заказчик с id = %s не имеет доступа к заказу с id = %s.", customerSelfDto.getCustomer_id(), order.getId()));
        }
    }

    @Transactional
    public ResponseEntity<String> acceptWork(CustomerSelfDto customerSelfDto) {
        OrderEntity order = orderService.findOrderById(customerSelfDto.getOrder_id());
        if (order.getCustomer().getId().equals(customerSelfDto.getCustomer_id())) {
            if ("WAITING_APPROVAL".equals(order.getOrderStatus().getCode())) {
                WorkEntity work = order.getWork();
                WorkerEntity worker = order.getWorker();
                work.setAcceptedByCustomer(true);
                workRepository.save(work);
                order.setWork(work);
                order.setOrderStatus(orderStatusService.findOrderStatusByCode("FINISHED"));
                worker.setNumberOfCompletedOrders(worker.getNumberOfCompletedOrders() + 1);
                workerService.saveWorker(worker);
                orderService.saveOrder(order);
                waitingWorkerService.deleteWaitingWorkersByOrderId(order.getId());
                return ResponseEntity.status(HttpStatus.OK)
                        .body(String.format("Заказ с id = %s успешно принят.", order.getId()));
            } else {
                throw new MethodNotAllowedException(String.format("Заказ имеет статус code = %s, принять заказ можно, если code = WAITING_APPROVAL.", order.getOrderStatus().getCode()));
            }
        } else {
            throw new NotEnoughRightsException(String.format("Заказчик с id = %s не имеет доступа к заказу с id = %s.", customerSelfDto.getCustomer_id(), order.getId()));
        }
    }
}
