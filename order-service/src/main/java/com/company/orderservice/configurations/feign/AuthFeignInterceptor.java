package com.company.orderservice.configurations.feign;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class AuthFeignInterceptor implements RequestInterceptor {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Logger logger = LoggerFactory.getLogger(AuthFeignInterceptor.class);
    private static final String USER_ID = "UserId";
    private static final String FULLNAME = "FullName";
    private static final String USER_ROLES = "UserRoles";

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header(USER_ID, UUID.randomUUID().toString());
        requestTemplate.header(FULLNAME, "sports-fight");
        try {
            requestTemplate.header(USER_ROLES, objectMapper.writeValueAsString(List.of("EXECUTOR")));
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
        }
    }
}