package com.company.workerservice.util;

import jakarta.ws.rs.ForbiddenException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;


public class SecurityContext {
    private SecurityContext() {}

    public static UUID getAuthorizedUserId() {
        Authentication authentication = getAuthentication();
        if (authentication != null) {
            return getId(authentication);
        } else {
            throw new ForbiddenException("FORBIDDEN");
        }
    }

    public static String getAuthorizedUserName() {
        Authentication authentication = getAuthentication();
        if (authentication != null) {
            return (String) authentication.getCredentials();
        } else {
            throw new ForbiddenException("FORBIDDEN");
        }
    }

    private static UUID getId(Authentication authentication) {
        return UUID.fromString(authentication.getPrincipal().toString());
    }

    private static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}