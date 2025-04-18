package ru.yandex.intershop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.yandex.intershop.service.CartService;
import ru.yandex.intershop.service.ItemService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;
    private final CartService cartService;

    @GetMapping("/{itemId}")
    public Mono<String> getItem(@PathVariable Long itemId,
                                Model model) {
        return itemService.findById(itemId)
                .map(itemDto -> {
                    model.addAttribute("item", itemDto);
                    return "item";
                });
    }

    @PostMapping("/{itemId}/plus")
    public Mono<String> changeCountPlus(@PathVariable Long itemId) {
        return cartService.changeItemQuantity(itemId, "plus")
                .then(Mono.just("redirect:/items/" + itemId));
    }

    @PostMapping("/{itemId}/minus")
    public Mono<String> changeCountMinus(@PathVariable Long itemId) {
        return cartService.changeItemQuantity(itemId, "minus")
                .then(Mono.just("redirect:/items/" + itemId));
    }
}
