package com.paf.fitness_app.config;


import com.paf.fitness_app.entity.RegistrationSource;
import com.paf.fitness_app.entity.UserEntity;
import com.paf.fitness_app.entity.UserRole;
import com.paf.fitness_app.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final UserService userService;

    @Value("${frontend.url}")
    private String frontendUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        if ("github".equals(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId())) {
            DefaultOAuth2User principal = (DefaultOAuth2User) authentication.getPrincipal();
            Map<String, Object> attributes = principal.getAttributes();
            String email = attributes.getOrDefault("email", "").toString();
            String name = attributes.getOrDefault("name", "").toString();

            userService.findByEmail(email).ifPresentOrElse(
                    user -> updateUser(user, attributes),
                    () -> createUser(email, name, attributes)
            );
        }
        setAlwaysUseDefaultTargetUrl(true);
        setDefaultTargetUrl(frontendUrl);
        super.onAuthenticationSuccess(request, response, authentication);
    }

    private void updateUser(UserEntity user, Map<String, Object> attributes) {
        // Update user information if necessary
        // For example, update user's name if it has changed on GitHub
        String name = attributes.getOrDefault("name", "").toString();
        if (!name.equals(user.getName())) {
            user.setName(name);
            userService.save(user);
        }
    }

    private void createUser(String email, String name, Map<String, Object> attributes) {
        // Create a new user
        UserEntity newUser = new UserEntity();
        newUser.setRole(UserRole.ROLE_USER);
        newUser.setEmail(email);
        newUser.setName(name);
        newUser.setSource(RegistrationSource.GITHUB);
        try {
            userService.save(newUser);
        } catch (Exception e) {
            // Handle save failure
            // Log error or throw a custom exception
            e.printStackTrace();
        }
    }
}
