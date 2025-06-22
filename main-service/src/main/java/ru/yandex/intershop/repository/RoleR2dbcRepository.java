package ru.yandex.intershop.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import ru.yandex.intershop.entity.Role;

@Repository
public interface RoleR2dbcRepository extends ReactiveCrudRepository<Role, Long> {

    Flux<Role> findByUserId(Long userId);
}
