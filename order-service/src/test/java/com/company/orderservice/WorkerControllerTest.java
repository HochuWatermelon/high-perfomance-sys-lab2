package com.company.orderservice;

import com.company.orderservice.model.dto.worker.WorkerPostDto;
import com.company.orderservice.model.dto.worker.WorkerPutDto;
import com.company.orderservice.model.entity.CustomerEntity;
import com.company.orderservice.model.entity.OrderEntity;
import com.company.orderservice.model.entity.OrderStatusEntity;
import com.company.orderservice.model.entity.ProfessionEntity;
import com.company.orderservice.model.entity.WorkerEntity;
import com.company.orderservice.model.entity.WorkerProfessionEntity;
import com.company.orderservice.model.enums.OrderStatusType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static com.company.orderservice.MonolitServiceApplication.parseUUIDFromString;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class WorkerControllerTest extends AbstractTest {

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
    void createWorkerTest() throws Exception {
        ProfessionEntity profession = new ProfessionEntity("Программист");
        professionRepository.save(profession);
        MvcResult mvcResult;
        WorkerPostDto worker = new WorkerPostDto("Сеньор", "Джава", "Оверфловович", "Программист", "Сеньор");
        mvcResult = mockMvc.perform(post("/worker")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(worker)))
                .andExpect(status().isCreated())
                .andReturn();

        String result = mvcResult.getResponse().getContentAsString();
        UUID workerId = parseUUIDFromString(result);
        assertThat(workerProfessionRepository.existsByWorkerIdAndProfessionId(workerId, profession.getId())).isTrue();
    }

    @Test
    void updateWorkerTest() throws Exception {
        WorkerEntity worker = new WorkerEntity("Сеньор", "Джава", "Оверфловович");
        workerRepository.save(worker);
        WorkerPutDto workerPutDto = new WorkerPutDto("Джун", "Паскаль", "Шпаргалович");
        mockMvc.perform(put("/worker/{workerId}", worker.getId())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(workerPutDto)))
                .andExpect(status().isOk());
    }

    @Test
    void addToWaitingListTest() throws Exception {
        OrderStatusEntity orderNewStatusEntity = new OrderStatusEntity("NEW", OrderStatusType.NEW);
        orderStatusRepository.save(orderNewStatusEntity);
        ProfessionEntity profession = new ProfessionEntity("Программист");
        professionRepository.save(profession);
        CustomerEntity customer = new CustomerEntity("Элиша", "Энефу", "Адуоджо");
        WorkerEntity worker = new WorkerEntity("Сеньор", "Джава", "Оверфловович");
        workerRepository.save(worker);
        customerRepository.save(customer);
        OrderEntity order = new OrderEntity(customer, profession, 747474, "Нужно создать бота в телеге.");
        order.setOrderStatus(orderNewStatusEntity);
        orderRepository.save(order);
        WorkerProfessionEntity workerProfession = new WorkerProfessionEntity();
        workerProfession.setWorker(worker);
        workerProfession.setProfession(profession);
        workerProfession.setRank("Хороший");
        workerProfessionRepository.save(workerProfession);
        mockMvc.perform(get("/order/get-new-orders/{workerId}", worker.getId())
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }



}
