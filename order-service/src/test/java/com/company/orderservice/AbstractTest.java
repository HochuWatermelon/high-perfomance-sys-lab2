package com.company.orderservice;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.company.orderservice.repository.CustomerRepository;
import com.company.orderservice.repository.FeedbackRepository;
import com.company.orderservice.repository.OrderRepository;
import com.company.orderservice.repository.OrderStatusRepository;
import com.company.orderservice.repository.ProfessionRepository;
import com.company.orderservice.repository.WaitingWorkerRepository;
import com.company.orderservice.repository.WorkRepository;
import com.company.orderservice.repository.WorkerProfessionRepository;
import com.company.orderservice.repository.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
public class AbstractTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected ProfessionRepository professionRepository;
    @Autowired
    protected WorkerProfessionRepository workerProfessionRepository;
    @Autowired
    protected WorkerRepository workerRepository;
    @Autowired
    protected OrderStatusRepository orderStatusRepository;
    @Autowired
    protected CustomerRepository customerRepository;
    @Autowired
    protected OrderRepository orderRepository;
    @Autowired
    protected WaitingWorkerRepository waitingWorkerRepository;
    @Autowired
    protected FeedbackRepository feedbackRepository;
    @Autowired
    protected WorkRepository workRepository;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>(
            "postgres:16"
    );
}
