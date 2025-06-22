package ru.yandex.intershop.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import ru.yandex.intershop.entity.User;

import java.util.Optional;

@Repository
public interface UserR2dbcRepository extends ReactiveCrudRepository<User, Long> {

    Mono<User> findByUsername(String username);
}
