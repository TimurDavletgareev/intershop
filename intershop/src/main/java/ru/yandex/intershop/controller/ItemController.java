package ru.yandex.intershop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.yandex.intershop.dto.ItemDto;
import ru.yandex.intershop.service.CartService;
import ru.yandex.intershop.service.ItemService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;
    private final CartService cartService;

    @GetMapping("/{itemId}")
    public String getItem(@PathVariable Long itemId,
                          Model model) {
        ItemDto itemDto = itemService.findById(itemId);
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
