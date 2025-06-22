package ru.yandex.intershop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.yandex.intershop.dto.ItemDto;
import ru.yandex.intershop.dto.OrderDto;
import ru.yandex.intershop.entity.Order;
import ru.yandex.intershop.error.exception.ConflictOnRequestException;
import ru.yandex.intershop.payment_client.PaymentSecuredService;
import ru.yandex.intershop.service.entity.OrderEntityService;

import java.security.Principal;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final OrderEntityService orderEntityService;
    private final CartService cartService;
    private final UserService userService;
    private final ItemService itemService;
    private final PaymentSecuredService paymentService;

    public Mono<OrderDto> buy(Principal principal) {
        log.info("Buy order");
        return cartService.find(principal)
                .publishOn(Schedulers.boundedElastic())
                .handle((cartDto, sink) -> {
                    Long userId = userService.getCurrentUserId(principal).block();
                    Integer balance = paymentService.getBalance(userId).block();
                    if (balance == null || cartDto.total() > balance) {
                        sink.error(new ConflictOnRequestException("Not enough balance"));
                        return;
                    }
                    List<ItemDto> items = cartDto.getItems();
                    OrderDto orderDto = new OrderDto();
                    String orderUid = UUID.randomUUID().toString();
                    orderDto.setOrderUid(orderUid);
                    orderDto.setItems(items);

                    paymentService.makePayment(userId, cartDto.total()).block();
                    List<Order> orderList = getOrders(items, orderUid, principal);
                    orderEntityService.saveAll(orderList).subscribe();
                    cartService.delete(userId).subscribe();
                    log.info("Buy order completed, orderUid = {}", orderUid);
                    sink.next(orderDto);
                });
    }

    private List<Order> getOrders(List<ItemDto> items, String orderUid, Principal principal) {
        List<Order> orderList = new ArrayList<>();
        Long userId = userService.getCurrentUserId(principal).block();
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

    public Mono<OrderDto> findByOrderUid(String orderUid, Principal principal) {
        log.info("Find order by orderUid = {}", orderUid);
        return orderEntityService.findByOrderUid(orderUid)
                .publishOn(Schedulers.boundedElastic())
                .collectList()
                .map(orders -> {
                    List<ItemDto> itemDtos = new ArrayList<>();
                    for (Order order : orders) {
                        ItemDto itemDto = itemService.findById(order.getItemId(), principal).block();
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

    public Mono<List<OrderDto>> find(Principal principal) {
        return userService.getCurrentUserId(principal)
                .publishOn(Schedulers.boundedElastic())
                .mapNotNull(
                        userId -> orderEntityService.findByUserId(userId)
                                .publishOn(Schedulers.boundedElastic())
                                .collectList()
                                .map(orders -> {
                                    log.info("Find all orders by userId = {}", userId);
                                    Set<String> orderUids = new HashSet<>();
                                    for (Order order : orders) {
                                        orderUids.add(order.getOrderUid());
                                    }
                                    List<OrderDto> orderDtos = new ArrayList<>();
                                    for (String orderUid : orderUids) {
                                        OrderDto orderDto = findByOrderUid(orderUid, principal).block();
                                        orderDtos.add(orderDto);
                                    }
                                    log.info("Found all orders by userId = {}, list size={}", userId, orderDtos.size());
                                    return orderDtos;
                                })
                                .block()
                )
                .defaultIfEmpty(new ArrayList<>());
    }
}
