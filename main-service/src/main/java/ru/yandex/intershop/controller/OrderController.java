package ru.yandex.intershop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;
import ru.yandex.intershop.service.OrderService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public Mono<String> getOrders(Model model) {
        return orderService.find()
                .map(orders -> {
                    model.addAttribute("orders", orders);
                    return "orders";
                });
    }

    @GetMapping("/{orderUid}")
    public Mono<String> getOrder(@PathVariable String orderUid, Model model) {
        return orderService.findByOrderUid(orderUid)
                .map(order -> {
                    model.addAttribute("order", order);
                    return "order";
                });
    }
}
