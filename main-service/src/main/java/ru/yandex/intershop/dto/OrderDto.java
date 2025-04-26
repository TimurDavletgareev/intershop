package ru.yandex.intershop.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderDto {

    private String orderUid;
    private List<ItemDto> items;
    private Integer totalSum;

    public Integer getTotalSum() {
        return this.items.stream().mapToInt(item -> item.getPrice() * item.getCount()).sum();
    }
}
