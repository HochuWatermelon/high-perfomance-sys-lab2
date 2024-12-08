package com.company.authservice.controllers.user.dto;

import java.util.UUID;

public record UserDto(UUID id,
                      String fullName,
                      boolean confirmed,
                      boolean blocked,
                      String role
                      ) {
}
