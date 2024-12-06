package com.company.monolitservice;

import com.company.monolitservice.model.dto.orderstatus.OrderStatusPostDto;
import com.company.monolitservice.model.enums.OrderStatusType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class OrderStatusControllerTest extends AbstractTest{


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
    void createOrderStatus() throws Exception {
        OrderStatusPostDto orderStatusPostDto = new OrderStatusPostDto("NEW", OrderStatusType.NEW);
        mockMvc.perform(post("/status")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(orderStatusPostDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void createNotValidOrderStatus() throws Exception {
        String requestBody = """
                {
                    "code": "INVALID",
                    "orderStatusType": "INVALID"
                }""";

        mockMvc.perform(post("/status")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

}
