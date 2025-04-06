package ru.yandex.intershop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.yandex.intershop.dto.ItemDto;
import ru.yandex.intershop.dto.OrderDto;
import ru.yandex.intershop.entity.Order;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final OrderEntityService orderEntityService;
    private final CartService cartService;
    private final UserService userService;
    private final ItemService itemService;

    public Mono<OrderDto> buy() {
        log.info("Buy order");
        return cartService.find()
                .publishOn(Schedulers.boundedElastic())
                .map(cartDto -> {
                    List<ItemDto> items = cartDto.getItems();
                    OrderDto orderDto = new OrderDto();
                    String orderUid = UUID.randomUUID().toString();
                    orderDto.setOrderUid(orderUid);
                    orderDto.setItems(items);

                    List<Order> orderList = getOrders(items, orderUid);
                    orderEntityService.saveAll(orderList).block();
                    cartService.deleteByUserId(userService.getCurrentUserId()).block();
                    log.info("Buy order completed, orderUid = {}", orderUid);
                    return orderDto;
                });
    }

    private List<Order> getOrders(List<ItemDto> items, String orderUid) {
        List<Order> orderList = new ArrayList<>();
        Long userId = userService.getCurrentUserId();
        for (ItemDto itemDto : items) {
            Order order = new Order();
            order.setOrderUid(orderUid);
            order.setItemId(itemDto.getId());
            order.setUserId(userId);
            order.setAmount(itemDto.getCount());
            order.setItemPrice(itemDto.getPrice());
            order.setTotalPrice(itemDto.getPrice() * itemDto.getCount());
            orderList.add(order);
        }
        return orderList;
    }

    public Mono<OrderDto> findByOrderUid(String orderUid) {
        log.info("Find order by orderUid = {}", orderUid);
        return orderEntityService.findByUserId(userService.getCurrentUserId())
                .publishOn(Schedulers.boundedElastic())
                .collectList()
                .map(orders -> {
                    List<ItemDto> itemDtos = new ArrayList<>();
                    for (Order order : orders) {
                        ItemDto itemDto = itemService.findById(order.getItemId()).block();
                        if (itemDto != null) {
                            itemDto.setPrice(order.getItemPrice());
                            itemDto.setCount(order.getAmount());
                            itemDto.setPrice(order.getItemPrice());
                            itemDtos.add(itemDto);
                        }
                    }
                    OrderDto orderDto = new OrderDto();
                    orderDto.setOrderUid(orderUid);
                    orderDto.setItems(itemDtos);
                    log.info("Found Order by orderUid = {}", orderUid);
                    return orderDto;
                });
    }

    public Mono<List<OrderDto>> find() {
        Long userId = userService.getCurrentUserId();
        log.info("Find all orders by userId = {}", userId);
        return orderEntityService.findByUserId(userId)
                .collectList()
                .publishOn(Schedulers.boundedElastic())
                .map(orders -> {
                    Set<String> orderUids = new HashSet<>();
                    for (Order order : orders) {
                        orderUids.add(order.getOrderUid());
                    }
                    List<OrderDto> orderDtos = new ArrayList<>();
                    for (String orderUid : orderUids) {
                        OrderDto orderDto = findByOrderUid(orderUid).block();
                        orderDtos.add(orderDto);
                    }
                    log.info("Found all orders by userId = {}, list size={}", userId, orderDtos.size());
                    return orderDtos;
                });
    }
}
