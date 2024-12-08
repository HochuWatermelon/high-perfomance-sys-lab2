package com.company.authservice.controllers.auth.dto;

import java.util.UUID;

public record AuthorizeUserResponse(
        UUID id,
        String fullName,
        String access,
        String refresh
) {
}
