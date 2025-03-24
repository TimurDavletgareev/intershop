package ru.yandex.intershop.mapper;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import ru.yandex.intershop.dto.PagingDto;
import ru.yandex.intershop.entity.Item;

@Component
public class PagingMapper {

    public PagingDto mapFrom(Page page) {
        PagingDto dto = new PagingDto();
        dto.setPageNumber(page.getNumber());
        dto.setPageSize(page.getSize());
        dto.setHasPrevious(page.hasPrevious());
        dto.setHasNext(page.hasNext());
        return dto;
    }
}
