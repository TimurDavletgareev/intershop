package ru.yandex.intershop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.yandex.intershop.service.CartService;
import ru.yandex.intershop.service.UserService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cart/items")
public class CartController {

    private final CartService cartService;
    private final UserService userService;

    @GetMapping
    public Mono<String> getCart(Model model) {
        //DEBUG
        userService.getCurrentUserId()
                .doOnNext(id -> System.out.println("CART CONTROLLER /{itemId} userId: " + id))
                .subscribe();
        // --DEBUG
        return cartService.find()
                .map(cartDto -> {
                    model.addAttribute("items", cartDto.getItems());
                    model.addAttribute("total", cartDto.total());
                    model.addAttribute("empty", cartDto.empty());
                    return "cart";
                });
    }

    @PostMapping("/{itemId}/plus")
    public Mono<String> changeCountPlus(@PathVariable Long itemId) {
        return cartService.changeItemQuantity(itemId, "plus")
                .then(Mono.just("redirect:/cart/items"));
    }

    @PostMapping("/{itemId}/minus")
    public Mono<String> changeCountMinus(@PathVariable Long itemId) {
        return cartService.changeItemQuantity(itemId, "minus")
                .then(Mono.just("redirect:/cart/items"));
    }

    @PostMapping("/{itemId}/delete")
    public Mono<String> delete(@PathVariable Long itemId) {
        return cartService.changeItemQuantity(itemId, "delete")
                .then(Mono.just("redirect:/cart/items"));
    }
}
