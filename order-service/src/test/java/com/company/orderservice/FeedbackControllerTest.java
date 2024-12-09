package com.company.orderservice;

import com.company.orderservice.model.dto.customer.CustomerSelfDto;
import com.company.orderservice.model.dto.feedback.FeedbackPostDto;
import com.company.orderservice.model.entity.CustomerEntity;
import com.company.orderservice.model.entity.FeedbackEntity;
import com.company.orderservice.model.entity.OrderEntity;
import com.company.orderservice.model.entity.OrderStatusEntity;
import com.company.orderservice.model.entity.ProfessionEntity;
import com.company.orderservice.model.entity.WorkerEntity;
import com.company.orderservice.model.enums.OrderStatusType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class FeedbackControllerTest extends AbstractTest{

    @BeforeEach
    public void cleanRepositories(){
        professionRepository.deleteAll();
        waitingWorkerRepository.deleteAll();
        workerRepository.deleteAll();
        orderRepository.deleteAll();
        orderStatusRepository.deleteAll();
        workerProfessionRepository.deleteAll();
        workRepository.deleteAll();
        customerRepository.deleteAll();
        feedbackRepository.deleteAll();
    }


    @Test
    void createFeedbackTest() throws Exception {
        OrderStatusEntity orderNewStatusEntity = new OrderStatusEntity("NEW", OrderStatusType.NEW);
        orderStatusRepository.save(orderNewStatusEntity);
        OrderStatusEntity orderPaidStatusEntity = new OrderStatusEntity("PAID", OrderStatusType.PAID);
        orderStatusRepository.save(orderPaidStatusEntity);
        OrderStatusEntity orderWithFeedbackStatusEntity = new OrderStatusEntity("WITH_FEEDBACK", OrderStatusType.WITH_FEEDBACK);
        orderStatusRepository.save(orderWithFeedbackStatusEntity);
        ProfessionEntity profession = new ProfessionEntity("Программист");
        professionRepository.save(profession);
        CustomerEntity customer = new CustomerEntity("Элиша", "Энефу", "Адуоджо");
        WorkerEntity worker = new WorkerEntity("Сеньор", "Джава", "Оверфловович");
        workerRepository.save(worker);
        customerRepository.save(customer);
        UUID customerId = customer.getId();
        OrderEntity order = new OrderEntity(customer, profession, 747474, "Нужно создать бота в телеге.");
        order.setWorker(worker);
        order.setOrderStatus(orderPaidStatusEntity);
        orderRepository.save(order);
        FeedbackPostDto feedbackPostDto = new FeedbackPostDto(new CustomerSelfDto(customerId, order.getId()), "Превосходная работа.", 5.0);
        mockMvc.perform(post("/feedback")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(feedbackPostDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void updateFeedbackTest() throws Exception {
        OrderStatusEntity orderNewStatusEntity = new OrderStatusEntity("NEW", OrderStatusType.NEW);
        orderStatusRepository.save(orderNewStatusEntity);
        OrderStatusEntity orderPaidStatusEntity = new OrderStatusEntity("PAID", OrderStatusType.PAID);
        orderStatusRepository.save(orderPaidStatusEntity);
        OrderStatusEntity orderWithFeedbackStatusEntity = new OrderStatusEntity("WITH_FEEDBACK", OrderStatusType.WITH_FEEDBACK);
        orderStatusRepository.save(orderWithFeedbackStatusEntity);
        ProfessionEntity profession = new ProfessionEntity("Программист");
        professionRepository.save(profession);
        CustomerEntity customer = new CustomerEntity("Элиша", "Энефу", "Адуоджо");
        WorkerEntity worker = new WorkerEntity("Сеньор", "Джава", "Оверфловович");
        workerRepository.save(worker);
        customerRepository.save(customer);
        UUID customerId = customer.getId();
        OrderEntity order = new OrderEntity(customer, profession, 747474, "Нужно создать бота в телеге.");
        order.setWorker(worker);
        order.setOrderStatus(orderPaidStatusEntity);
        orderRepository.save(order);
        FeedbackEntity feedback = new FeedbackEntity("Норм.", 4.0);
        feedback.setWorker(worker);
        feedback.setOrder(order);
        feedbackRepository.save(feedback);
        order.setOrderStatus(orderWithFeedbackStatusEntity);
        orderRepository.save(order);
        String responseBody = String.format("""
                {
                    "customerSelfDto": {
                        "customerId": "%s",
                        "orderId": "%s"
                    },
                    "feedback": "ПАСИБА!",
                    "score": 3.68
                }""", customerId, order.getId());
        mockMvc.perform(put("/feedback/{feedbackId}", feedback.getId())
                .contentType("application/json")
                .content(responseBody))
                .andExpect(status().isOk());
    }

}
