package ru.yandex.intershop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import ru.yandex.intershop.service.entity.RoleEntityService;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoleService {

    private final RoleEntityService roleEntityService;

    public List<SimpleGrantedAuthority> getRoles(Long userId) {
        log.info("Find GrantedAuthorities by userId: {}", userId);
        return roleEntityService.getRoles(userId)
                .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                .collectList()
                .block();
    }
}
