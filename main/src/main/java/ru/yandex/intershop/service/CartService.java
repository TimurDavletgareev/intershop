package ru.yandex.intershop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.yandex.intershop.dto.CartDto;
import ru.yandex.intershop.dto.ItemDto;
import ru.yandex.intershop.entity.CartPosition;
import ru.yandex.intershop.entity.Item;
import ru.yandex.intershop.mapper.ItemMapper;
import ru.yandex.intershop.service.entity.CartEntityService;
import ru.yandex.intershop.service.entity.ItemEntityService;

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

    public Mono<CartDto> find() {
        log.info("Find cartPositions");
        CartDto cartDto = new CartDto();
        Long userId = userService.getCurrentUserId();
        return cartEntityService.findByUserId(userId)
                .publishOn(Schedulers.boundedElastic())
                .map(cartPositions -> {
                    Map<Long, Integer> quantityByItemId = new HashMap<>();
                    for (CartPosition cartPosition : cartPositions) {
                        quantityByItemId.put(cartPosition.getItemId(), cartPosition.getAmount());
                    }
                    cartDto.setQuantityByItemId(quantityByItemId);
                    List<Long> itemIds = cartPositions.stream().map(CartPosition::getItemId).toList();
                    List<Item> items = itemEntityService.findByIdIn(itemIds).toStream().toList();
                    List<ItemDto> cartItems = new ArrayList<>();
                    for (Item item : items) {
                        ItemDto itemDto = itemMapper.map(item);
                        itemDto.setCount(quantityByItemId.get(item.getId()));
                        cartItems.add(itemDto);
                    }
                    cartDto.setItems(cartItems);
                    log.info("UserCartContent by userId={} found, size={}", userId, cartItems.size());
                    return cartDto;
                });
    }

    public Mono<Map<Long, Integer>> getQuantities(Page<ItemDto> page) {
        return find()
                .map(cartDto -> {
                    Map<Long, Integer> quantityByItemIdOnPage = new HashMap<>();
                    if (cartDto != null && !cartDto.empty()) {
                        Map<Long, Integer> quantityByItemIdInCart = cartDto.getQuantityByItemId();
                        page.forEach(itemDto ->
                                quantityByItemIdOnPage.put(itemDto.getId(),
                                        quantityByItemIdInCart.getOrDefault(itemDto.getId(), 0)));
                    } else {
                        page.forEach(itemDto ->
                                quantityByItemIdOnPage.put(itemDto.getId(), 0));
                    }
                    return quantityByItemIdOnPage;
                });
    }

    public Mono<Integer> getCountByItemId(Long itemId) {
        Long userId = userService.getCurrentUserId();
        return cartEntityService.findByUserIdAndItemId(userId, itemId)
                .map(CartPosition::getAmount)
                .defaultIfEmpty(0);
    }

    public Mono<Void> changeItemQuantity(Long itemId, String action) {
        log.info("Changing item quantity in user cartPosition by itemId: {}, action={}", itemId, action);
        Long userId = userService.getCurrentUserId();
        return cartEntityService.findByUserIdAndItemId(userId, itemId)
                .publishOn(Schedulers.boundedElastic())
                .defaultIfEmpty(new CartPosition())
                .doOnNext(cartPosition -> {
                    Item item = itemEntityService.findById(itemId).block();
                    log.info("Changing item quantity: action={}, item={}, cartPosition={}",
                            action, item, cartPosition);
                    if (cartPosition.getId() == null && action.equalsIgnoreCase(AmountAction.PLUS.name())) {
                        log.info("Setting itemId={} to new cart position of userId={}", itemId, userId);
                        cartPosition = new CartPosition();
                        cartPosition.setUserId(userId);
                        cartPosition.setItemId(itemId);
                        if (item != null) {
                            cartPosition.setItemPrice(item.getPrice());
                        }
                        cartPosition.setAmount(1);
                        cartEntityService.save(cartPosition).block();
                        return;
                    }
                    if (cartPosition.getId() == null) {
                        return;
                    }
                    int newQuantity = getNewQuantity(action, cartPosition, item);
                    if (newQuantity == 0) {
                        cartEntityService.deleteById(cartPosition.getId()).block();
                        return;
                    }
                    cartPosition.setAmount(newQuantity);
                    cartEntityService.save(cartPosition).block();
                })
                .then();
    }

    private static int getNewQuantity(String action, CartPosition cartPosition, Item item) {
        int oldQuantity = cartPosition.getAmount();
        int newQuantity = 0;
        if (action.equalsIgnoreCase(AmountAction.PLUS.name())
                && cartPosition.getAmount() < item.getAmountInStock()) {
            newQuantity = cartPosition.getAmount() + 1;
        } else if (action.equalsIgnoreCase(AmountAction.MINUS.name())
                && oldQuantity > 0) {
            newQuantity = oldQuantity - 1;
        }
        return newQuantity;
    }

    public Mono<Void> delete() {
        Long userId = userService.getCurrentUserId();
        return cartEntityService.deleteByUserId(userId);
    }

    private enum AmountAction {
        PLUS,
        MINUS
    }
}
