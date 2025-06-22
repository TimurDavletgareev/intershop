package ru.yandex.intershop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.yandex.intershop.mapper.PagingMapper;
import ru.yandex.intershop.service.ItemService;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/public")
@Slf4j
public class PublicController {

    private final ItemService itemService;
    private final PagingMapper pagingMapper;

    @GetMapping
    public Mono<String> findPaging(@RequestParam(value = "search", required = false) String search,
                                   @RequestParam(value = "sort", defaultValue = "NO") String sort,
                                   @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                   @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                   Model model) {
        return itemService.find(search, sort, pageNumber, pageSize)
                .publishOn(Schedulers.boundedElastic())
                .doOnNext(page -> {
                    model.addAttribute("paging", pagingMapper.mapFrom(page));
                    model.addAttribute("items", itemService.createItemsLists(page));
                })
                .map(page -> "public");
    }

    @GetMapping("/{itemId}")
    public Mono<String> getItem(@PathVariable Long itemId,
                                Model model,
                                Principal principal) {
        return itemService.findById(itemId, principal)
                .map(itemDto -> {
                    model.addAttribute("item", itemDto);
                    return "public-item";
                });
    }
}
