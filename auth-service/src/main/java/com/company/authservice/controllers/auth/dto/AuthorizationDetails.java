package com.company.authservice.controllers.auth.dto;

import com.company.authservice.models.enums.Role;

import java.util.List;
import java.util.UUID;

public record AuthorizationDetails(UUID id,
                                   String fullName,
                                   List<Role> roles) {
}
