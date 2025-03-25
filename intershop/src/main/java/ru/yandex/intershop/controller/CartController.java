package ru.yandex.intershop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.yandex.intershop.dto.CartDto;
import ru.yandex.intershop.service.CartService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cart/items")
public class CartController {

    private final CartService cartService;

    @GetMapping
    public String getCart(Model model) {
        CartDto cartDto = cartService.find();
        model.addAttribute("items", cartDto.getItems());
        model.addAttribute("total", cartDto.total());
        model.addAttribute("empty", cartDto.empty());
        return "cart";
    }

    @PostMapping("/{itemId}")
    public String changeCount(@PathVariable Long itemId,
                              @RequestParam String action) {
        cartService.changeItemQuantity(itemId, action);
        return "redirect:/cart/items";
    }
}
