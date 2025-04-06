package ru.yandex.intershop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.yandex.intershop.service.CartService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cart/items")
public class CartController {

    private final CartService cartService;

    @GetMapping
    public Mono<String> getCart(Model model) {
        return cartService.find()
                .map(cartDto -> {
                    model.addAttribute("items", cartDto.getItems());
                    model.addAttribute("total", cartDto.total());
                    model.addAttribute("empty", cartDto.empty());
                    return "cart";
                });
    }

    @PostMapping("/{itemId}")
    public Mono<String> changeCount(@PathVariable Long itemId,
                                    @RequestParam String action) {
        return cartService.changeItemQuantity(itemId, action)
                .then(Mono.just("redirect:/cart/items"));
    }
}
