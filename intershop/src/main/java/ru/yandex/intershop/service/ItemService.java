package ru.yandex.intershop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.yandex.intershop.dto.ItemDto;
import ru.yandex.intershop.entity.Item;
import ru.yandex.intershop.mapper.ItemMapper;
import ru.yandex.intershop.util.PageRequestCreator;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemService {

    private final ItemEntityService itemEntityService;
    private final CartService cartService;
    private final ItemMapper itemMapper;
    private static final int ROW_SIZE = 3;

    public Mono<ItemDto> findById(Long itemId) {
        log.info("Find ItemDto by itemId: {}", itemId);
        return itemEntityService.findById(itemId)
                .publishOn(Schedulers.boundedElastic())
                .map(itemMapper::map)
                .map(itemDto -> {
                    Integer count = cartService.getCountByItemId(itemId).block();
                    itemDto.setCount(count);
                    return itemDto;
                });
    }

    public Mono<Page<ItemDto>> find(String searchString, String sortString, int pageNumber, int pageSize) {
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
        Flux<Item> itemFlux;
        if (searchString == null || searchString.isEmpty()) {
            itemFlux = itemEntityService.findAll(pageable);
        } else {
            itemFlux = itemEntityService.findByTitle(searchString, pageable);
        }
        /*CartDto cartDto = cartService.find().block();
        Map<Long, Integer> userItems = cartDto.getQuantityByItemId();*/
        return itemFlux
                .publishOn(Schedulers.boundedElastic())
                .map(itemMapper::map)
                .collectList()
                .zipWith(itemEntityService.count())
                .map(p -> new PageImpl<>(p.getT1(), pageable, p.getT2()));
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
