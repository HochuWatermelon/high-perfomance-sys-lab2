package com.company.authservice;

import com.company.authservice.model.dto.order.OrderPostDto;
import com.company.authservice.model.dto.order.OrderPutDto;
import com.company.authservice.model.entity.CustomerEntity;
import com.company.authservice.model.entity.OrderEntity;
import com.company.authservice.model.entity.OrderStatusEntity;
import com.company.authservice.model.entity.ProfessionEntity;
import com.company.authservice.model.enums.OrderStatusType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class OrderControllerTest extends AbstractTest{

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
    void createOrderTest() throws Exception {
        OrderStatusEntity orderStatusEntity = new OrderStatusEntity("NEW", OrderStatusType.NEW);
        orderStatusRepository.save(orderStatusEntity);
        ProfessionEntity profession = new ProfessionEntity("Программист");
        professionRepository.save(profession);
        CustomerEntity customer = new CustomerEntity("Элиша", "Энефу", "Адуоджо");
        customerRepository.save(customer);
        UUID customerId = customer.getId();

        OrderPostDto orderPostDto = new OrderPostDto("Программист", 747575, "Сделать вторую лабораторную по ВС.");
        mockMvc.perform(post("/order/{customerId}", customerId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(orderPostDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void updateOrderTest() throws Exception {
        OrderStatusEntity orderStatusEntity = new OrderStatusEntity("NEW", OrderStatusType.NEW);
        orderStatusRepository.save(orderStatusEntity);
        ProfessionEntity profession = new ProfessionEntity("Программист");
        professionRepository.save(profession);
        CustomerEntity customer = new CustomerEntity("Элиша", "Энефу", "Адуоджо");
        customerRepository.save(customer);
        OrderEntity order = new OrderEntity(customer, profession, 747474, "Нужно создать бота в телеге.");
        order.setOrderStatus(orderStatusEntity);
        orderRepository.save(order);

        OrderPutDto orderPutDto = new OrderPutDto(747474*2, "Нужно сделать 2 ботов в телеге.");
        mockMvc.perform(put("/order/{orderId}", order.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(orderPutDto)))
                .andExpect(status().isOk());
    }

}
