package ru.yandex.intershop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.yandex.intershop.dto.ItemDto;
import ru.yandex.intershop.dto.PagingDto;
import ru.yandex.intershop.mapper.PagingMapper;
import ru.yandex.intershop.service.CartService;
import ru.yandex.intershop.service.ItemService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/main")
public class MainController {

    private final ItemService itemService;
    private final CartService cartService;
    private final PagingMapper pagingMapper;

    @GetMapping("/items")
    public String findAll(@RequestParam(value = "search", required = false) String search,
                          @RequestParam(value = "sort", defaultValue = "NO") String sort,
                          @RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
                          @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                          Model model) {
        Page<ItemDto> pagedItems = itemService.find(search, sort, pageNumber, pageSize);
        List<List<ItemDto>> itemLists = itemService.createItemsLists(pagedItems);
        PagingDto pagingDto = pagingMapper.mapFrom(pagedItems);
        model.addAttribute("paging", pagingDto);
        model.addAttribute("items", itemLists);
        return "main";
    }

    @PostMapping("/items/{itemId}")
    public String changeCount(@PathVariable Long itemId,
                              @RequestParam String action) {
        cartService.changeItemQuantity(itemId, action);
        return "redirect:/main/items";
    }
}
