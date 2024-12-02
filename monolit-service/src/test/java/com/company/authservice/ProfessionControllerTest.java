package com.company.authservice;


import com.company.authservice.model.dto.profession.ProfessionPostDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProfessionControllerTest extends AbstractTest{


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
    void createProfessionTest() throws Exception {
        ProfessionPostDto professionPostDto = new ProfessionPostDto("Репетитор");
        mockMvc.perform(post("/profession")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(professionPostDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void createNotValidProfession() throws Exception {
        String requestBody = """
                {
                    "name": " ",
                }""";

        mockMvc.perform(post("/profession")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }


}
