package ru.yandex.intershop.dto;

import lombok.Data;

@Data
public class OrderDto {

    private Long id;
    private String orderUid;
    private Long userId;
    private Long itemId;
    private Integer amount;
    private Integer itemPrice;
    private Integer totalPrice;
}
