package ru.yandex.intershop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.intershop.dto.CartDto;
import ru.yandex.intershop.dto.ItemDto;
import ru.yandex.intershop.entity.CartPosition;
import ru.yandex.intershop.entity.Item;
import ru.yandex.intershop.mapper.ItemMapper;

import java.util.ArrayList;
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
        log.info("Find cartPositions");
        CartDto cartDto = new CartDto();
        Long userId = userService.getCurrentUserId();
        List<CartPosition> cartPositions = cartEntityService.findByUserId(userId);
        Map<Long, Integer> quantityByItemId = new HashMap<>();
        for (CartPosition cartPosition : cartPositions) {
            quantityByItemId.put(cartPosition.getItemId(), cartPosition.getAmount());
        }
        cartDto.setQuantityByItemId(quantityByItemId);
        List<Long> itemIds = cartPositions.stream().map(CartPosition::getItemId).toList();
        List<Item> items = itemEntityService.findByIdIn(itemIds);
        List<ItemDto> cartItems = new ArrayList<>();
        for (Item item : items) {
            ItemDto itemDto = itemMapper.map(item);
            itemDto.setCount(quantityByItemId.get(item.getId()));
            cartItems.add(itemDto);
        }
        cartDto.setItems(cartItems);
        log.info("UserCartContent by userId={} found, size={}", userId, cartItems.size());
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
        if (newQuantity == 0) {
            cartEntityService.delete(cartPosition.getId());
            return;
        }
        cartPosition.setAmount(newQuantity);
        cartEntityService.save(cartPosition);
    }

    public void deleteByUserId(Long userId) {
        cartEntityService.deleteByUserId(userId);
    }
}
