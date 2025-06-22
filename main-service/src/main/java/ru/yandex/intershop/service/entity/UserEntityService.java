package ru.yandex.intershop.service.entity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import ru.yandex.intershop.entity.User;
import ru.yandex.intershop.repository.UserR2dbcRepository;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class UserEntityService {

    private final UserR2dbcRepository userR2dbcRepository;

    public Mono<User> findById(Long id) {
        log.info("Find user by id: {}", id);
        return userR2dbcRepository.findById(id);
    }

    public Mono<User> findByUsername(String username) {
        log.info("Find user by username: {}", username);
        return userR2dbcRepository.findByUsername(username);
    }
}
