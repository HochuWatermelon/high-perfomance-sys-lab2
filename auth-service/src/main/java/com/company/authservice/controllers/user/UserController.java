package com.company.authservice.controllers.user;

import lombok.RequiredArgsConstructor;
import com.company.authservice.controllers.user.dto.UserDto;
import com.company.authservice.mappers.UserMapper;
import com.company.authservice.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserMapper userMapper;
    private final UserService userService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<UserDto> find(@PathVariable UUID id) {
        return userService.find(id).map(userMapper::convert);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Mono<Boolean> exists(@RequestParam String fullName) {
        return userService.isExists(fullName);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable UUID id) {
        return userService.delete(id);
    }


    @PatchMapping("/{id}/block")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('SUPERVISOR')")
    public Mono<Void> block(@PathVariable UUID id) {
        return userService.block(id).then();
    }

    @DeleteMapping("/{id}/block")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('SUPERVISOR')")
    public Mono<Void> unblock(@PathVariable UUID id) {
        return userService.unblock(id).then();
    }

    @PatchMapping("/{id}/confirm")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('SUPERVISOR')")
    public Mono<Void> confirm(@PathVariable UUID id) {
        return userService.confirm(id).then();
    }

}
