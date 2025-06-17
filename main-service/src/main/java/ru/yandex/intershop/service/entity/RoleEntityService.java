package ru.yandex.intershop.service.entity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import ru.yandex.intershop.entity.Role;
import ru.yandex.intershop.repository.RoleR2dbcRepository;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class RoleEntityService {

    private final RoleR2dbcRepository roleR2dbcRepository;

    public Flux<Role> getRoles(Long userId) {
        log.info("Find roles by userId: {}", userId);
        return roleR2dbcRepository.findByUserId(userId);
    }
}
