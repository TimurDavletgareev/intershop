package ru.yandex.intershop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.yandex.intershop.mapper.PagingMapper;
import ru.yandex.intershop.service.CartService;
import ru.yandex.intershop.service.ItemService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/main")
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
                .doOnNext(page -> {
                    model.addAttribute("paging", pagingMapper.mapFrom(page));
                    model.addAttribute("items", itemService.createItemsLists(page));
                })
                .map(page -> "main");
    }

    @PostMapping("/items/{itemId}")
    public Mono<String> changeCount(@PathVariable Long itemId,
                              @RequestParam String action) {
        return cartService.changeItemQuantity(itemId, action)
                .then(Mono.just("redirect:/main/items"));

    }
}
