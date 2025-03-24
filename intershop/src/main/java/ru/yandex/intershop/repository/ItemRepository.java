package ru.yandex.intershop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.intershop.entity.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
}
