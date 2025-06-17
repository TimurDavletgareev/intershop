package ru.yandex.intershop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.yandex.intershop.service.entity.UserEntityService;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements ReactiveUserDetailsService {

    private static final Long CURRENT_USER_ID = 1L;

    private final UserEntityService userEntityService;
    private final RoleService roleService;

    public Long getCurrentUserId() {
        log.info("Getting current user id");
        Long currentUserId = CURRENT_USER_ID;
        log.info("Current user id is {}", currentUserId);
        return currentUserId;
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) throws UsernameNotFoundException {
        // Загружаем сущность User из базы данных
        return userEntityService.findByUsername(username)
                .map(user -> new org.springframework.security.core.userdetails.User(
                        user.getUsername(), user.getPassword(), roleService.getRoles(user.getId())));
    }
}
