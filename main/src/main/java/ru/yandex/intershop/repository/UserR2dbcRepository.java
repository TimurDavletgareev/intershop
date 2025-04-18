package ru.yandex.intershop.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.intershop.entity.User;

@Repository
public interface UserR2dbcRepository extends ReactiveCrudRepository<User, Long> {
}
