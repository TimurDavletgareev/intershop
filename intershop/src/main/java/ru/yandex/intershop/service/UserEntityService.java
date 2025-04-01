package ru.yandex.intershop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.intershop.entity.User;
import ru.yandex.intershop.repository.UserR2dbcRepository;

import java.util.Optional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class UserEntityService {

    private final UserR2dbcRepository userR2dbcRepository;

    public User findById(Long id) {
        log.info("Find user by id: {}", id);
        Optional<User> user = userR2dbcRepository.findById(id);
        if (user.isPresent()) {
            log.info("User by id={} found successfully", id);
            return user.get();
        }
        return null;
    }
}
