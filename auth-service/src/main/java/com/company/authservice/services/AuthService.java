package com.company.authservice.services;

import lombok.RequiredArgsConstructor;
import com.company.authservice.configurations.security.AuthenticationProvider;
import com.company.authservice.controllers.auth.dto.AuthorizationDetails;
import com.company.authservice.controllers.auth.dto.AuthorizeUserResponse;
import com.company.authservice.exceptions.ErrorCode;
import com.company.authservice.exceptions.InternalException;
import com.company.authservice.models.User;
import com.company.authservice.models.enums.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;
import java.util.UUID;


@RequiredArgsConstructor
@Service
public class AuthService {
    private final UserService userService;
    private final PasswordEncoder encoder;
    private final AuthenticationProvider authProvider;
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Transactional
    public Mono<UUID> register(User user) {
        logger.info("Registering user: {}", user);
        return userService.isExists(user.getFullName()).flatMap(result -> {
            if (result) {
                return Mono.error(new InternalException(HttpStatus.BAD_REQUEST, ErrorCode.USER_ALREADY_EXIST));
            }
            user.setPassword(encoder.encode(user.getPassword()));
            user.setConfirmed(true);
            return userService.create(user).map(User::getId);
        });
    }


    public Mono<AuthorizationDetails> getAuthDetails() {
        logger.info("Getting auth details");
        return ReactiveSecurityContextHolder.getContext()
                .map(context -> {
                    Authentication authentication = context.getAuthentication();
                    UUID id = UUID.fromString((String) authentication.getPrincipal());
                    String username = (String) authentication.getCredentials();
                    List<String> role = authentication.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority).toList();
                    return new AuthorizationDetails(id, username, role.stream().map(Role::valueOf)
                            .toList());
                });
    }

    public Mono<AuthorizeUserResponse> authorize(User authUser) {
        logger.info("Authorizing user: {}", authUser);
        return userService.find(authUser.getFullName()).flatMap(user -> {
            String encodedAuthUserPassword = encoder.encode(authUser.getPassword());
            if (encoder.matches(user.getPassword(), encodedAuthUserPassword)) {
                return Mono.error(
                        new InternalException(HttpStatus.UNAUTHORIZED, ErrorCode.USER_PASSWORD_INCORRECT));
            }
            return createAuthorizeUserResponse(user);
        });
    }

    public Mono<AuthorizeUserResponse> reAuthorize(String refreshToken){
        logger.info("Re-authorizing user: {}", refreshToken);
        if (!authProvider.isValid(refreshToken)) {
            throw new InternalException(HttpStatus.UNAUTHORIZED, ErrorCode.TOKEN_EXPIRED);
        }
        UUID id = UUID.fromString(authProvider.getIdFromToken(refreshToken));
        return userService.find(id).flatMap(this::createAuthorizeUserResponse);
    }

    private Mono<AuthorizeUserResponse> createAuthorizeUserResponse(User user) {
        return Mono.just(new AuthorizeUserResponse(
                user.getId(),
                user.getFullName(),
                authProvider.createAccessToken(user.getId(), user.getFullName(), Set.of(user.getRole())),
                authProvider.createRefreshToken(user.getId(), user.getFullName())
        ));
    }
}
