package ru.yandex.intershop.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.intershop.dto.ItemDto;
import ru.yandex.intershop.entity.Item;

@Component
public class ItemMapper {

    public ItemDto map(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setTitle(item.getTitle());
        itemDto.setDescription(item.getDescription());
        itemDto.setImgPath(item.getImageUrl());
        itemDto.setPrice(item.getPrice());
        return itemDto;
    }
}
