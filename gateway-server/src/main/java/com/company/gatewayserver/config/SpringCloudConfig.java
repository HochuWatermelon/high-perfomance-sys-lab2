package com.company.gatewayserver.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringCloudConfig {

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("monolit-service", r -> r.path("/monolit-service/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://monolit-service:8089"))
                .build();
    }
}
