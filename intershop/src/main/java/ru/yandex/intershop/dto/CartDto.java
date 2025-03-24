package ru.yandex.intershop.dto;

import lombok.Data;
import ru.yandex.intershop.entity.Item;

import java.util.Map;

@Data
public class CartDto {

    private Long userId;
    private Map<Item, Integer> items;
}
