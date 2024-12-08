package com.company.authservice.mappers;

import com.company.authservice.controllers.auth.dto.AuthorizeUserRequest;
import com.company.authservice.controllers.auth.dto.RegisterUserRequest;
import com.company.authservice.models.User;
import com.company.authservice.models.enums.Role;
import org.springframework.stereotype.Component;

@Component
public class AuthMapper {

    public User convert(RegisterUserRequest request) {
        return User.builder()
                .fullName(request.fullName())
                .password(request.password())
                .role(Role.valueOf(request.role()))
                .confirmed(true).blocked(false).build();
    }

    public User convert(AuthorizeUserRequest request) {
        return User.builder()
                .fullName(request.fullName())
                .password(request.password())
                .build();
    }
}