package com.example.borrowit.config;

import com.example.borrowit.JwtUtil;
import com.example.borrowit.service.impl.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    public OAuth2LoginSuccessHandler(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        // Créer l'utilisateur s'il n'existe pas
        userService.createUserIfNotExists(email, name);

        // Générer un JWT
        String token = jwtUtil.generateToken(userService.loadUserByUsername(email));

        // Rediriger vers Angular avec le token
        response.sendRedirect("http://localhost:4200/login/success?token=" + token);
    }
}
