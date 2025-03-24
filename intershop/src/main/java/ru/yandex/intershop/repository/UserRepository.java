package ru.yandex.intershop.repository;

import org.springframework.data.repository.CrudRepository;
import ru.yandex.intershop.entity.User;

public interface UserRepository extends CrudRepository<User, Long> {
}
