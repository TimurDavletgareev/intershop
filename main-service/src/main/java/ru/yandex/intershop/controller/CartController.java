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

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cart/items")
public class CartController {

    private final CartService cartService;

    @GetMapping
    public Mono<String> getCart(Model model, Principal principal) {
        return cartService.find(principal)
                .map(cartDto -> {
                    model.addAttribute("items", cartDto.getItems());
                    model.addAttribute("total", cartDto.total());
                    model.addAttribute("empty", cartDto.empty());
                    return "cart";
                });
    }

    @PostMapping("/{itemId}/plus")
    public Mono<String> changeCountPlus(@PathVariable Long itemId, Principal principal) {
        return cartService.changeItemQuantity(itemId, "plus", principal)
                .then(Mono.just("redirect:/cart/items"));
    }

    @PostMapping("/{itemId}/minus")
    public Mono<String> changeCountMinus(@PathVariable Long itemId, Principal principal) {
        return cartService.changeItemQuantity(itemId, "minus", principal)
                .then(Mono.just("redirect:/cart/items"));
    }

    @PostMapping("/{itemId}/delete")
    public Mono<String> delete(@PathVariable Long itemId, Principal principal) {
        return cartService.changeItemQuantity(itemId, "delete", principal)
                .then(Mono.just("redirect:/cart/items"));
    }
}
