package ru.yandex.intershop.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class CartDto {

    private List<ItemDto> items;
    private Map<Long, Integer> quantityByItemId;

    public int total() {
        if (items == null || items.isEmpty()) {
            return 0;
        }
        int total = 0;
        for (ItemDto item : items) {
            total += item.getPrice() * item.getCount();
        }
        return total;
    }

    public boolean empty() {
        return items == null || items.isEmpty();
    }
}
