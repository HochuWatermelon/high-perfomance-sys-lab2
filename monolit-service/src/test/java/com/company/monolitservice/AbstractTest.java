package com.company.monolitservice;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.company.monolitservice.repository.CustomerRepository;
import com.company.monolitservice.repository.FeedbackRepository;
import com.company.monolitservice.repository.OrderRepository;
import com.company.monolitservice.repository.OrderStatusRepository;
import com.company.monolitservice.repository.ProfessionRepository;
import com.company.monolitservice.repository.WaitingWorkerRepository;
import com.company.monolitservice.repository.WorkRepository;
import com.company.monolitservice.repository.WorkerProfessionRepository;
import com.company.monolitservice.repository.WorkerRepository;
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
