package ru.yandex.intershop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.yandex.intershop.dto.CartDto;
import ru.yandex.intershop.service.CartService;
import ru.yandex.intershop.service.ItemService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cart/items")
public class CartController {

    private final ItemService itemService;
    private final CartService cartService;

    @GetMapping
    public String getItem(Model model) {
        CartDto cartDto = cartService.find();
        model.addAttribute("item", itemDto);
        return "item";
    }

    @PostMapping("/{itemId}")
    public String changeCount(@PathVariable Long itemId,
                              @RequestParam String action) {
        cartService.changeItemQuantity(itemId, action);
        return "redirect:/items/" + itemId;
    }
}
