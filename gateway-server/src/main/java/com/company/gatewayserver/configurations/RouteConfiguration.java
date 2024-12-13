package com.company.gatewayserver.configurations;

import com.company.gatewayserver.filters.AuthenticationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;

@Configuration
public class RouteConfiguration {

    @Bean
    public RouteLocator routeLocator(
            RouteLocatorBuilder route,
            ServiceUrlsProperties props,
            AuthenticationFilter authFilter,
            @Value("${server.api.prefix}") String apiPrefix
    ) {
        return route.routes()
                .route(props.getAuthService() + "-route-auth", r -> r
                        .path(apiPrefix + "/auth/**")
                        .filters(f -> f
                                .stripPrefix(1)
                                .circuitBreaker(c -> c
                                        .setName(props.getAuthService() + "-circuit-breaker")
                                        .setFallbackUri(URI.create("forward:/fallback"))
                                )
                        )
                        .uri("lb://" + props.getAuthService())
                )
                .route(props.getAuthService() + "-route-users", r -> r
                        .path(apiPrefix + "/users/**")
                        .filters(f -> f
                                .stripPrefix(1)
                                .circuitBreaker(c -> c
                                        .setName(props.getAuthService() + "-circuit-breaker")
                                        .setFallbackUri(URI.create("forward:/fallback"))
                                )
                                .filter(authFilter.apply(new AuthenticationFilter.Config()))
                        )
                        .uri("lb://" + props.getAuthService())
                )
                .route(props.getOrderService() + "-route", r -> r
                        .path(apiPrefix + "/order/**",
                                apiPrefix + "/status/**",
                                apiPrefix + "/work/**")
                        .filters(f -> f
                                .stripPrefix(1)
                                .circuitBreaker(cb -> cb
                                        .setName(props.getOrderService() + "-circuit-breaker")
                                        .setFallbackUri(URI.create("forward:/fallback"))
                                )
                                 .filter(authFilter.apply(new AuthenticationFilter.Config()))
                        )
                        .uri("lb://" + props.getOrderService())
                )
                .route(props.getWorkerService() + "-route", r -> r
                        .path(apiPrefix + "/profession/**",
                                apiPrefix + "/worker/**",
                                apiPrefix + "/worker-profession/**")
                        .filters(f -> f
                                .stripPrefix(1)
                                .circuitBreaker(cb -> cb
                                        .setName(props.getWorkerService() + "-circuit-breaker")
                                        .setFallbackUri(URI.create("forward:/fallback"))
                                )
                                .filter(authFilter.apply(new AuthenticationFilter.Config()))
                        )
                        .uri("lb://" + props.getWorkerService())
                )
                .build();
    }
}