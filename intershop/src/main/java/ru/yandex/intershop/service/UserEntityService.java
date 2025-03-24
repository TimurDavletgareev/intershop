package ru.yandex.intershop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.intershop.entity.User;
import ru.yandex.intershop.repository.UserRepository;

import java.util.Optional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class UserEntityService {

    private final UserRepository userRepository;

    public User findById(Long id) {
        log.info("Find user by id: {}", id);
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            log.info("User by id={} found successfully", id);
            return user.get();
        }
        return null;
    }
}
