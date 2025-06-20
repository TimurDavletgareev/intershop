package ru.yandex.intershop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.yandex.intershop.service.entity.UserEntityService;

import java.security.Principal;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements ReactiveUserDetailsService {

    private static final Long ANONYMOUS_USER_ID = -1L;

    private final UserEntityService userEntityService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public Long getAnonymousUserId() {
        return ANONYMOUS_USER_ID;
    }

    public Mono<Long> getCurrentUserId(Principal principal) {
        log.info("Getting current user id");
        Mono<Long> currentUserId = userEntityService.findByUsername(principal.getName())
                .map(user -> {
                    if (user == null) {
                        return ANONYMOUS_USER_ID;
                    }
                    return user.getId();
                });
        return currentUserId
                .map(id -> {
                    id = id == null ? ANONYMOUS_USER_ID : id;
                    log.info("Current user id is {}", id);
                    return id;
                });
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) throws UsernameNotFoundException {
        log.info("Getting UserDetails by username: {}", username);
        Mono<UserDetails> result = userEntityService.findByUsername(username)
                .publishOn(Schedulers.boundedElastic())
                .map(user -> new org.springframework.security.core.userdetails.User(
                        user.getUsername(),
                        passwordEncoder.encode((user.getPassword())), //кодируем, т.к. пользователь создаётся в бд на старте
                        roleService.getRoles(user.getId())
                ));
        return result
                .map(userDetails -> {
                    log.info("Found UserDetails by username: {} -> {}", username, userDetails);
                    return userDetails;
                });
    }
}
