package ru.yandex.intershop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;
import ru.yandex.intershop.service.UserService;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class RedirectController {

    private final UserService userService;

    @GetMapping
    public Mono<String> authenticationRedirect(Principal principal) {
        if (principal == null) {
            return Mono.just("redirect:/public");
        }
        return userService.getCurrentUserId(principal)
                .map(id -> {
                    if (id.equals(userService.getAnonymousUserId())) {
                        return "redirect:/public";
                    }
                    return "redirect:/main/items";
                });
    }
}
