package com.company.authservice.mappers;

import com.company.authservice.controllers.user.dto.UserDto;
import com.company.authservice.models.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDto convert(User user) {
        return new UserDto(
                user.getId(),
                user.getFullName(),
                user.isConfirmed(),
                user.isBlocked(),
                user.getRole().name()
        );
    }
}
