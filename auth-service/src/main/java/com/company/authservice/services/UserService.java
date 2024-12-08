package com.company.authservice.services;

import lombok.RequiredArgsConstructor;
import com.company.authservice.exceptions.ErrorCode;
import com.company.authservice.exceptions.InternalException;
import com.company.authservice.models.User;
import com.company.authservice.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    public Mono<Boolean> isExists(String fullName) {
        logger.info("Checking if user exists with fullName {}", fullName);
        return userRepository.existsByFullName(fullName);
    }

    public Mono<User> find(UUID id) {
        logger.info("Find user by id {}", id);
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new InternalException(HttpStatus.NOT_FOUND, ErrorCode.USER_NOT_FOUND)));
    }


    public Mono<User> find(String fullName) {
        logger.info("Find user by fullName {}", fullName);
        return userRepository.findByFullName(fullName)
                .switchIfEmpty(Mono.error(new InternalException(HttpStatus.NOT_FOUND, ErrorCode.USER_NOT_FOUND)));
    }


    public Mono<User> create(User user) {
        user.setId(null);
        logger.info("User id {}", user.getId());
        return userRepository.save(user)
                .doOnSuccess(savedUser -> logger.info("User successfully saved: {}", savedUser))
                .doOnError(error -> logger.error("Error while saving user: {}", error.getMessage(), error));
    }


    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Mono<User> block(UUID id) {
        logger.info("Block user with id {}", id);
        return find(id).flatMap(user -> {
            user.setBlocked(true);
            return userRepository.save(user);
        });
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Mono<User> unblock(UUID id) {
        logger.info("Unblock user with id {}", id);
        return find(id).flatMap(user -> {
            user.setBlocked(false);
            return userRepository.save(user);
        });
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Mono<User> confirm(UUID id) {
        logger.info("Confirm user with id {}", id);
        return find(id).flatMap(user -> {
            user.setConfirmed(true);
            return userRepository.save(user);
        });
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Mono<Void> delete(String fullName) {
        logger.info("Delete user with name {}", fullName);
        return find(fullName).flatMap(userRepository::delete);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Mono<Void> delete(UUID id) {
        logger.info("Delete user with id {}", id);
        return find(id).flatMap(userRepository::delete);
    }
}