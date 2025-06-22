package ru.yandex.intershop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;
import ru.yandex.intershop.service.CartService;
import ru.yandex.intershop.service.ItemService;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;
    private final CartService cartService;

    @GetMapping("/{itemId}")
    public Mono<String> getItem(@PathVariable Long itemId,
                                Model model,
                                Principal principal) {
        return itemService.findById(itemId, principal)
                .map(itemDto -> {
                    model.addAttribute("item", itemDto);
                    return "item";
                });
    }

    @PostMapping("/{itemId}/plus")
    public Mono<String> changeCountPlus(@PathVariable Long itemId, Principal principal) {
        return cartService.changeItemQuantity(itemId, "plus", principal)
                .then(Mono.just("redirect:/items/" + itemId));
    }

    @PostMapping("/{itemId}/minus")
    public Mono<String> changeCountMinus(@PathVariable Long itemId, Principal principal) {
        return cartService.changeItemQuantity(itemId, "minus", principal)
                .then(Mono.just("redirect:/items/" + itemId));
    }
}
