package ru.yandex.intershop.dto;

import lombok.Data;
import ru.yandex.intershop.entity.Item;

import java.util.List;

@Data
public class CartDto {

    private Long userId;
    private List<Item> items;
}
