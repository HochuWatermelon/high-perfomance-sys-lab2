package com.company.authservice.repositories;

import com.company.authservice.models.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserRepository extends ReactiveCrudRepository<User, UUID> {

    Mono<Boolean> existsByFullName(String fullName);

    Mono<User> findByFullName(String fullName);
}
