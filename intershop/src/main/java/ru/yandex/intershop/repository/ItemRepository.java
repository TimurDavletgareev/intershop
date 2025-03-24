package ru.yandex.intershop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.intershop.entity.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    Page<Item> findByTitleContainsIgnoreCase(String title, Pageable pageable);

    Page<Item> findByPriceBetween(Integer priceAfter, Integer priceBefore, Pageable pageable);

    Integer findMaxPrice();
}
