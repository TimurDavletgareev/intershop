package ru.yandex.intershop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import reactor.core.publisher.Mono;
import ru.yandex.intershop.dto.OrderDto;
import ru.yandex.intershop.service.OrderService;

@Controller
@RequiredArgsConstructor
public class BuyController {

    private final OrderService orderService;

    @PostMapping("/buy")
    public Mono<String> getOrder(Model model) {
        return orderService.buy()
                        .map(orderDto -> {
                            model.addAttribute("order", orderDto);
                            model.addAttribute("newOrder", true);
                            return "order";
                        });
    }
}
