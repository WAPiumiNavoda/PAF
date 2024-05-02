package com.paf.fitness_app.config;

import com.paf.fitness_app.entity.UserEntity;
import com.paf.fitness_app.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


@Component
public class AuthenticationFacade {

    private final UserService userService;

    public AuthenticationFacade(UserService userService) {
        this.userService = userService;
    }

    public Long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserEntity) {
                UserEntity user = (UserEntity) principal;
                return user.getId();
            } else if (principal instanceof String) {
                String userEmail = (String) principal;
                UserEntity user = userService.findByEmail(userEmail).orElse(null);
                if (user != null) {
                    return user.getId();
                }
            }
        }
        return null;
    }
}


