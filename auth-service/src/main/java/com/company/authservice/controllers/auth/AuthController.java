package com.company.authservice.controllers.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.company.authservice.controllers.auth.dto.*;
import com.company.authservice.mappers.AuthMapper;
import com.company.authservice.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthMapper authMapper;
    private final AuthService authService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize(("hasAnyAuthority('SUPERVISOR')"))
    public Mono<RegisterUserResponse> register(@Valid @RequestBody Mono<RegisterUserRequest> request) {
        return request.map(authMapper::convert)
                .flatMap(authService::register)
                .map(RegisterUserResponse::new);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("isAuthenticated()")
    public Mono<AuthorizationDetails> getAuthDetails() {
        return authService.getAuthDetails();
    }


    @PostMapping("/tokens")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<AuthorizeUserResponse> authorize(@Valid @RequestBody Mono<AuthorizeUserRequest> request) {
        return request.map(authMapper::convert)
                .flatMap(authService::authorize);
    }

    @PostMapping("/tokens/refresh")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<AuthorizeUserResponse> reAuthorize(@RequestParam String refresh)  {
        return authService.reAuthorize(refresh);
    }

}