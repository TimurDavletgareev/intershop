package ru.yandex.intershop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.yandex.intershop.dto.CartDto;
import ru.yandex.intershop.dto.ItemDto;
import ru.yandex.intershop.entity.Item;
import ru.yandex.intershop.mapper.ItemMapper;
import ru.yandex.intershop.util.PageRequestCreator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemService {

    private final ItemEntityService itemEntityService;
    private final CartService cartService;
    private final ItemMapper itemMapper;
    private static final int ROW_SIZE = 3;

    public ItemDto findById(Long itemId) {
        log.info("Find ItemDto by itemId: {}", itemId);
        ItemDto itemDto = itemMapper.map(itemEntityService.findById(itemId));
        CartDto cartDto = cartService.find();
        Map<Long, Integer> userItems = cartDto.getQuantityByItemId();
        itemDto.setCount(userItems.getOrDefault(itemId, 0));
        return itemDto;
    }

    public Page<ItemDto> find(String searchString, String sortString, int pageNumber, int pageSize) {
        log.info("Search pagedItems by searchString={}, sortString={}, pageNumber={}, pageSize={}",
                searchString, sortString, pageNumber, pageSize);
        Sort sort = Sort.by("id");
        if (sortString.equalsIgnoreCase(ItemSort.ALPHA.name())) {
            sort = Sort.by("title");
        }
        if (sortString.equalsIgnoreCase(ItemSort.PRICE.name())) {
            sort = Sort.by("price");
        }
        Pageable pageable = PageRequestCreator.create(pageNumber, pageSize, sort);
        Page<Item> pagedItems;
        if (searchString == null || searchString.isEmpty()) {
            pagedItems = itemEntityService.findAll(pageable);
        } else {
            pagedItems = itemEntityService.findByTitle(searchString, pageable);
        }
        Page<ItemDto> pagedItemDtos = pagedItems.map(itemMapper::map);

        CartDto cartDto = cartService.find();
        Map<Long, Integer> quantityByItemId = cartDto.getQuantityByItemId();
        for (ItemDto itemDto : pagedItemDtos) {
            itemDto.setCount(quantityByItemId.getOrDefault(itemDto.getId(), 0));
        }
        log.info("Found {} pagedItems by searchString={}, sortString={}, pageNumber={}, pageSize={}",
                pagedItemDtos.getTotalElements(), searchString, sortString, pageNumber, pageSize);
        return pagedItemDtos;
    }

    public List<List<ItemDto>> createItemsLists(Page<ItemDto> page) {
        List<List<ItemDto>> itemsLists = new ArrayList<>();
        int i = 0;
        int numberOfElementsOnPage = page.getNumberOfElements();
        while (i < numberOfElementsOnPage) {
            List<ItemDto> row = new ArrayList<>(ROW_SIZE);
            for (int j = 0; j < ROW_SIZE; j++) {
                if (i >= numberOfElementsOnPage) {
                    break;
                }
                row.add(page.getContent().get(i));
                i++;
            }
            itemsLists.add(row);
        }
        return itemsLists;
    }

    private enum ItemSort {
        NO,
        ALPHA,
        PRICE
    }
}
