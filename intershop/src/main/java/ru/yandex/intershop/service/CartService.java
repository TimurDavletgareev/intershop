package ru.yandex.intershop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.intershop.dto.CartDto;
import ru.yandex.intershop.dto.ItemDto;
import ru.yandex.intershop.entity.CartPosition;
import ru.yandex.intershop.entity.Item;
import ru.yandex.intershop.mapper.ItemMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartService {

    private final UserService userService;
    private final CartEntityService cartEntityService;
    private final ItemEntityService itemEntityService;
    private final ItemMapper itemMapper;

    public CartDto find() {
        log.info("Find cartContent");
        Long userId = userService.getCurrentUserId();
        Map<ItemDto, Integer> cartItems = new HashMap<>();
        List<CartPosition> cartContent = cartEntityService.findByUserId(userId);
        List<Long> itemIds = cartContent.stream().map(CartPosition::getItemId).toList();
        List<Item> items = itemEntityService.findByIdIn(itemIds);
        for (Item item : items) {
            ItemDto itemDto = itemMapper.mapFrom(item);
            cartItems.put(itemDto, cartItems.getOrDefault(itemDto, 0));
        }
        CartDto cartDto = new CartDto();
        cartDto.setUserItems(cartItems);
        log.info("UserCartContent by userId={} found, size={}", userId, cartContent.size());
        return cartDto;
    }

    public void changeItemQuantity(Long itemId, String action) {
        log.info("Changing item quantity in user cartPosition by itemId: {}, action={}", itemId, action);
        Long userId = userService.getCurrentUserId();
        Item item = itemEntityService.findById(itemId);
        CartPosition cartPosition = cartEntityService.findByUserIdAndItemId(userId, itemId);
        if (cartPosition == null && action.equalsIgnoreCase(AmountAction.PLUS.name())) {
            cartPosition = new CartPosition();
            cartPosition.setUserId(userId);
            cartPosition.setItemId(itemId);
            cartPosition.setItemPrice(item.getPrice());
            cartPosition.setAmount(1);
            cartEntityService.save(cartPosition);
            return;
        }
        if (cartPosition == null) {
            return;
        }
        int oldQuantity = cartPosition.getAmount();
        int newQuantity = 0;
        if (action.equalsIgnoreCase(AmountAction.PLUS.name())
                && cartPosition.getAmount() < item.getAmountInStock()) {
            newQuantity = cartPosition.getAmount() + 1;
        } else if (action.equalsIgnoreCase(AmountAction.MINUS.name())
                && oldQuantity > 0) {
            newQuantity = oldQuantity - 1;
        }
        cartPosition.setAmount(newQuantity);
        cartEntityService.save(cartPosition);
    }

}
