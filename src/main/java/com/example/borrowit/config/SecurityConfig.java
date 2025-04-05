package com.example.borrowit.config;

import com.example.borrowit.JwtAuthorizationFilter;
import com.example.borrowit.JwtUtil;
import com.example.borrowit.service.impl.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter.XFrameOptionsMode;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public SecurityConfig(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        JwtAuthorizationFilter jwtAuthorizationFilter = new JwtAuthorizationFilter(authenticationManager, userService, jwtUtil);

        http
                .csrf(csrf -> csrf.disable())  // Désactive la protection CSRF (utile pour les API REST)
                .cors()  // Active la configuration CORS
                .and()
                .authorizeRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()  // Permet l'accès à l'authentification
                        .requestMatchers("/api/users/**").permitAll()  // Permet l'accès à l'endpoint des utilisateurs
                        .requestMatchers("/api/forgot-password", "/api/reset-password").permitAll()  // Permet l'accès à la réinitialisation de mot de passe
                        .requestMatchers("/api/test/auth-status").authenticated()  // Nécessite une authentification pour ce chemin
                        .anyRequest().authenticated()  // Toute autre requête nécessite une authentification
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // Désactive la gestion des sessions (utilise JWT)
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)  // Ajoute le filtre d'autorisation JWT avant l'authentification standard
                .headers(headers -> headers
                        .addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsMode.SAMEORIGIN))  // Protection contre les attaques de type clickjacking
                        .contentSecurityPolicy(csp -> csp
                                .policyDirectives("frame-ancestors 'self' http://localhost:4200")  // Politique de sécurité des contenus (CSP)
                        )
                        .xssProtection(xss -> xss
                                .headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK)  // Protection contre les attaques XSS
                        )
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // Utilisation de BCrypt pour l'encodage des mots de passe
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();  // Retourne l'AuthenticationManager pour la gestion des authentifications
    }
}
