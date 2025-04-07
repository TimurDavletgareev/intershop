package ru.yandex.intershop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.yandex.intershop.mapper.PagingMapper;
import ru.yandex.intershop.service.CartService;
import ru.yandex.intershop.service.ItemService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/main")
@Slf4j
public class MainController {

    private final ItemService itemService;
    private final CartService cartService;
    private final PagingMapper pagingMapper;

    @GetMapping("/items")
    public Mono<String> findPaging(@RequestParam(value = "search", required = false) String search,
                                   @RequestParam(value = "sort", defaultValue = "NO") String sort,
                                   @RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
                                   @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                   Model model) {
        return itemService.find(search, sort, pageNumber, pageSize)
                .publishOn(Schedulers.boundedElastic())
                .doOnNext(page -> {
                    model.addAttribute("paging", pagingMapper.mapFrom(page));
                    model.addAttribute("items", itemService.createItemsLists(page));
                    model.addAttribute("quantities", cartService.getQuantities(page).block());
                })
                .map(page -> "main");
    }

    @PostMapping("/items/{itemId}/minus")
    public Mono<String> changeCountMinus(@PathVariable Long itemId) {
        return cartService.changeItemQuantity(itemId, "minus")
                .then(Mono.just("redirect:/main/items"));
    }

    @PostMapping("/items/{itemId}/plus")
    public Mono<String> changeCountPlus(@PathVariable Long itemId) {
        return cartService.changeItemQuantity(itemId, "plus")
                .then(Mono.just("redirect:/main/items"));
    }
}
