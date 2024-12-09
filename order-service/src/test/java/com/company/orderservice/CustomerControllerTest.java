package com.company.orderservice;


import com.company.orderservice.model.dto.customer.CustomerPostDto;
import com.company.orderservice.model.dto.customer.CustomerPutDto;
import com.company.orderservice.model.dto.customer.CustomerSelfDto;
import com.company.orderservice.model.entity.CustomerEntity;
import com.company.orderservice.model.entity.OrderEntity;
import com.company.orderservice.model.entity.OrderStatusEntity;
import com.company.orderservice.model.entity.ProfessionEntity;
import com.company.orderservice.model.entity.WaitingWorkerEntity;
import com.company.orderservice.model.entity.WorkerEntity;
import com.company.orderservice.model.entity.WorkerProfessionEntity;
import com.company.orderservice.model.enums.OrderStatusType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class CustomerControllerTest extends AbstractTest{

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
    void createCustomerIntegrationTest() throws Exception {
        CustomerPostDto customerDto = new CustomerPostDto("John", "Doe", "Smith");

        mockMvc.perform(post("/customer")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(customerDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void updateCustomerTest() throws Exception {
        CustomerEntity customer = new CustomerEntity("Элиша", "Энефу", "Адуоджо");
        customerRepository.save(customer);
        CustomerPutDto customerDto = new CustomerPutDto("Ок", "Ладно", "Хорошо");
        mockMvc.perform(put("/customer/{customerId}", customer.getId())
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(customerDto)))
                .andExpect(status().isOk());
    }

    @Test
    void addWorkerToOrderTest() throws Exception {
        OrderStatusEntity orderNewStatusEntity = new OrderStatusEntity("NEW", OrderStatusType.NEW);
        orderStatusRepository.save(orderNewStatusEntity);
        OrderStatusEntity orderInWorkStatusEntity = new OrderStatusEntity("IN_WORK", OrderStatusType.IN_WORK);
        orderStatusRepository.save(orderInWorkStatusEntity);
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
        WaitingWorkerEntity waitingWorker = new WaitingWorkerEntity();
        waitingWorker.setWorker(worker);
        waitingWorker.setOrder(order);
        waitingWorkerRepository.save(waitingWorker);
        mockMvc.perform(put("/customer/order/add-worker-to-order/{workerId}", worker.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(new CustomerSelfDto(customer.getId(), order.getId()))))
                .andExpect(status().isOk());
        OrderEntity updatedOrder = orderRepository.findById(order.getId()).get();
        assertThat(updatedOrder.getOrderStatus()).isEqualTo(orderInWorkStatusEntity);
    }

    @Test
    void createBadCustomerIntegrationTest() throws Exception {
        CustomerPostDto customerDto = new CustomerPostDto("fjfjfjfjfjkdjkfjdkfjdkfjdkfjdkfjkdjfkdjfkdjfkjdkfjkdjf", null, " ");
                mockMvc.perform(post("/customer")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(customerDto)))
                .andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    void updateBadCustomerTest() throws Exception {
        CustomerEntity customer = new CustomerEntity("Ок", "Ладно", "Хорошо");
        customerRepository.save(customer);
        CustomerPutDto customerDto = new CustomerPutDto("", "jfjfjfjfjfjfjfjfjfjfjfjfjfjfjfjfjfjffjfjfjfjfjfjfj", null);
        mockMvc.perform(put("/customer/{customerId}", customer.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(customerDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getWaitingWorkersTest() throws Exception {
        CustomerEntity customer = new CustomerEntity("Ок", "Ладно", "Хорошо");
        OrderStatusEntity orderNewStatusEntity = new OrderStatusEntity("NEW", OrderStatusType.NEW);
        orderStatusRepository.save(orderNewStatusEntity);
        ProfessionEntity profession = new ProfessionEntity("Программист");
        professionRepository.save(profession);
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
        WaitingWorkerEntity waitingWorker = new WaitingWorkerEntity(worker, order);
        waitingWorkerRepository.save(waitingWorker);
        String requestBody = String.format("""
                {
                    "customer_self_dto": {
                        "customer_id": "%s",
                        "order_id": "%s"
                    },
                    "pageable_get_dto": {
                        "page": 0,
                        "size": 50
                    }
                }""", customer.getId(), order.getId());
        mockMvc.perform(get("/customer/order/get-waiting-workers", customer.getId())
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isOk());
    }

    @Test
    void notFoundWaitingWorkersTest() throws Exception {
        CustomerEntity customer = new CustomerEntity("Ок", "Ладно", "Хорошо");
        OrderStatusEntity orderNewStatusEntity = new OrderStatusEntity("NEW", OrderStatusType.NEW);
        orderStatusRepository.save(orderNewStatusEntity);
        ProfessionEntity profession = new ProfessionEntity("Программист");
        professionRepository.save(profession);
        customerRepository.save(customer);
        OrderEntity order = new OrderEntity(customer, profession, 747474, "Нужно создать бота в телеге.");
        order.setOrderStatus(orderNewStatusEntity);
        orderRepository.save(order);
        String requestBody = String.format("""
                {
                    "customer_self_dto": {
                        "customer_id": "%s",
                        "order_id": "%s"
                    },
                    "pageable_get_dto": {
                        "page": 0,
                        "size": 50
                    }
                }""", customer.getId(), order.getId());
        mockMvc.perform(get("/customer/order/get-waiting-workers", customer.getId())
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isNotFound());
    }

}
