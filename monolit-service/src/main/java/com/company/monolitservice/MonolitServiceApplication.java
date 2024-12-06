package com.company.monolitservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.util.UUID;

@SpringBootApplication
@EnableFeignClients
public class MonolitServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MonolitServiceApplication.class, args);
    }

    public static UUID parseUUIDFromString(String response){
        int left = response.indexOf("id = ") + 5;
        String response2 = response.substring(left);
        String id = response2.substring(0, 36);
        return UUID.fromString(id);
    }
}
