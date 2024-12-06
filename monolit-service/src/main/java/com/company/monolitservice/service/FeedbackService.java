package com.company.monolitservice.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.company.monolitservice.exceptions.FeedbackNotFoundException;
import com.company.monolitservice.exceptions.MethodNotAllowedException;
import com.company.monolitservice.exceptions.NotEnoughRightsException;
import com.company.monolitservice.model.dto.customer.CustomerSelfDto;
import com.company.monolitservice.model.dto.feedback.FeedbackDto;
import com.company.monolitservice.model.dto.feedback.FeedbackPostDto;
import com.company.monolitservice.model.dto.feedback.FeedbackPutDto;
import com.company.monolitservice.model.dto.pageable.PageableGetDto;
import com.company.monolitservice.model.entity.FeedbackEntity;
import com.company.monolitservice.model.entity.OrderEntity;
import com.company.monolitservice.model.entity.WorkerEntity;
import com.company.monolitservice.model.mapper.FeedbackMapper;
import com.company.monolitservice.repository.FeedbackRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final FeedbackMapper feedbackMapper;
    private final WorkerService workerService;
    private final OrderService orderService;
    private final OrderStatusService orderStatusService;

    @Transactional
    public ResponseEntity<String> createFeedback(FeedbackPostDto feedbackDto) {
        FeedbackEntity feedback = feedbackMapper.feedbackPostDtoToEntity(feedbackDto);
        CustomerSelfDto customerDto = feedbackDto.getCustomer_self_dto();
        OrderEntity order = orderService.findOrderById(customerDto.getOrder_id());
        if (order.getCustomer().getId().equals(customerDto.getCustomer_id())) {
            if ("PAID".equals(order.getOrderStatus().getCode())) {
                WorkerEntity worker = order.getWorker();
                double newWorkerRating;
                order.setOrderStatus(orderStatusService.findOrderStatusByCode("WITH_FEEDBACK"));
                orderService.saveOrder(order);
                feedback.setOrder(order);
                feedback.setWorker(worker);
                feedbackRepository.save(feedback);
                worker.addFeedback(feedback);
                newWorkerRating = workerService.calculateWorkerRating(worker);
                worker.setRating(newWorkerRating);
                workerService.saveWorker(worker);
                return ResponseEntity.status(HttpStatus.CREATED)
                                     .body(String.format("Отзыв на заказ с id = %s успешно создан", customerDto.getOrder_id()));
            } else {
                throw new MethodNotAllowedException(String.format("Заказ имеет статус code = %s, оставить отзыв на заказ можно, если code = PAID.", order.getOrderStatus().getCode()));
            }
        } else {
            throw new NotEnoughRightsException(String.format("Заказчик с id = %s не имеет доступа к заказу с id = %s.", customerDto.getCustomer_id(), order.getId()));
        }
    }

    public FeedbackEntity findFeedbackById(UUID feedbackId) {
        return feedbackRepository.findById(feedbackId).orElseThrow(() ->
                new FeedbackNotFoundException(String.format("Отзыв с id = %s не был найден", feedbackId)));
    }


    @Transactional
    public ResponseEntity<String> updateFeedback(UUID feedbackId, FeedbackPutDto feedbackDto) {
        FeedbackEntity feedbackEntity = findFeedbackById(feedbackId);
        CustomerSelfDto customerDto = feedbackDto.getCustomer_self_dto();
        OrderEntity order = orderService.findOrderById(customerDto.getOrder_id());
        if (order.getCustomer().getId().equals(customerDto.getCustomer_id())) {
            if ("WITH_FEEDBACK".equals(order.getOrderStatus().getCode())) {
                WorkerEntity worker = order.getWorker();
                double newWorkerRating;
                feedbackMapper.updateEntityFromFeedbackPutDto(feedbackEntity, feedbackDto);
                feedbackRepository.save(feedbackEntity);
                worker.updateFeedback(feedbackEntity.getId(), feedbackEntity);
                newWorkerRating = workerService.calculateWorkerRating(worker);
                worker.setRating(newWorkerRating);
                workerService.saveWorker(worker);
                return ResponseEntity.status(HttpStatus.OK)
                                     .body(String.format("Отзыв с id = %s был изменён", feedbackId));
            } else {
                throw new MethodNotAllowedException(String.format("Заказ имеет статус code = %s, изменять отзыв на заказ можно, если code = WITH_FEEDBACK.", order.getOrderStatus().getCode()));
            }
        } else {
            throw new NotEnoughRightsException(String.format("Заказчик с id = %s не имеет доступа к заказу с id = %s.", customerDto.getCustomer_id(), order.getId()));
        }
    }

    public ResponseEntity<String> getWorkerFeedbacks(UUID workerId, PageableGetDto pageableDto) {
        WorkerEntity worker = workerService.findWorkerById(workerId);
        if (!worker.getFeedbacks().isEmpty()) {
            Pageable pageable = PageRequest.of(pageableDto.getPage(), pageableDto.getSize());
            Page<FeedbackEntity> feedbackPage = feedbackRepository.findAllByWorkerId(workerId, pageable);
            if (pageable.getPageNumber() >= feedbackPage.getTotalPages()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("На этой странице данных нет, попробуйте меньшее значение page или size.");
            }
            String response = feedbackPage.getContent().stream().map(feedbackEntity -> {
                FeedbackDto feedbackDto = feedbackMapper.feedbackEntityToFeedbackDto(feedbackEntity);
                return feedbackDto.toString();
            }).collect(Collectors.joining("\n\n"));
            return ResponseEntity.status(HttpStatus.OK)
                    .body(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("У вас нет отзывов, попробуйте брать в работу больше заказов.");
        }
    }

    public ResponseEntity<String> deleteFeedbackByOrder(CustomerSelfDto customerDto, UUID feedbackId) {
        FeedbackEntity feedback = findFeedbackById(feedbackId);
        if (customerDto.getCustomer_id().equals(feedback.getOrder().getCustomer().getId())) {
            feedbackRepository.delete(feedback);
            OrderEntity order = orderService.findOrderById(customerDto.getOrder_id());
            order.setOrderStatus(orderStatusService.findOrderStatusByCode("PAID"));
            orderService.saveOrder(order);
            WorkerEntity worker = order.getWorker();
            double newWorkerRating;
            worker.deleteFeedback(feedback);
            newWorkerRating = workerService.calculateWorkerRating(worker);
            worker.setRating(newWorkerRating);
            workerService.saveWorker(worker);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(String.format("Отзыв с id = %s был удалён", feedbackId));
        } else {
            throw new NotEnoughRightsException(String.format("Заказчик с id = %s не имеет доступа к заказу с id = %s.", customerDto.getCustomer_id(), customerDto.getOrder_id()));
        }
    }
}
