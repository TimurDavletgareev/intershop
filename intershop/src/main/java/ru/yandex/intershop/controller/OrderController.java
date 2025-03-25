package ru.yandex.intershop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.yandex.intershop.dto.OrderDto;
import ru.yandex.intershop.service.OrderService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public String getOrders(Model model) {
        List<OrderDto> orders = orderService.find();
        model.addAttribute("orders", orders);
        return "orders";
    }

    @GetMapping("/{orderUid}")
    public String getOrder(@PathVariable String orderUid, Model model) {
        OrderDto order = orderService.findByOrderUid(orderUid);
        model.addAttribute("order", order);
        return "order";
    }
}
