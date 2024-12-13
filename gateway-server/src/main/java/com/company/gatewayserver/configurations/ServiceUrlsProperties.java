package com.company.gatewayserver.configurations;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "spring.services.urls")
public class ServiceUrlsProperties {

    private final String authService;
    private final String orderService;
    private final String workerService;

    public ServiceUrlsProperties(String authService, String orderService, String workerService) {
        this.authService = authService;
        this.orderService = orderService;
        this.workerService = workerService;
    }

}