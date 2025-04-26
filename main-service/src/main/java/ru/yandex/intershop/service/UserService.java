package ru.yandex.intershop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.intershop.repository.UserR2dbcRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private static final Long CURRENT_USER_ID = 1L;

    private final UserR2dbcRepository userR2dbcRepository;

    public Long getCurrentUserId() {
        log.info("Getting current user id");
        Long currentUserId = CURRENT_USER_ID;
        log.info("Current user id is {}", currentUserId);
        return currentUserId;
    }
}
