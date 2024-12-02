package com.company.authservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import java.util.UUID;

@SpringBootApplication
public class AuthServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }

    public static UUID parseUUIDFromString(String response){
        int left = response.indexOf("id = ") + 5;
        String response2 = response.substring(left);
        String id = response2.substring(0, 36);
        return UUID.fromString(id);
    }
}
