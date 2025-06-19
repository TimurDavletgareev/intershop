package ru.yandex.intershop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.yandex.intershop.service.entity.UserEntityService;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements ReactiveUserDetailsService {

    private static final Long ANONYMOUS_USER_ID = -1L;

    private final UserEntityService userEntityService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public Mono<Long> getCurrentUserId() {
        log.info("Getting current user id");
        Mono<Long> currentUserId = ReactiveSecurityContextHolder.getContext()
                .map(context -> context.getAuthentication().getName())
                .mapNotNull(name -> userEntityService.findByUsername(name)
                        .map(user -> {
                            if (user == null) {
                                return ANONYMOUS_USER_ID;
                            }
                            return user.getId();
                        })
                        .block()
                )
                .defaultIfEmpty(ANONYMOUS_USER_ID);
        return currentUserId
                .map(id -> {
                    log.info("Current user id is {}", currentUserId);
                    return id == null ? ANONYMOUS_USER_ID : id;
                });
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) throws UsernameNotFoundException {
        log.info("Getting UserDetails by username: {}", username);
        return userEntityService.findByUsername(username)
                .publishOn(Schedulers.boundedElastic())
                .map(user -> new org.springframework.security.core.userdetails.User(
                        user.getUsername(),
                        passwordEncoder.encode((user.getPassword())), //кодируем, т.к. пользователь создаётся в бд на старте
                        roleService.getRoles(user.getId())
                ));
    }
}
