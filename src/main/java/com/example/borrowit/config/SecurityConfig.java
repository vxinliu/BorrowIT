package com.example.borrowit.config;

import com.example.borrowit.JwtAuthorizationFilter;
import com.example.borrowit.JwtUtil;
import com.example.borrowit.service.impl.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
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
                // Désactivation de CSRF (une seule ligne suffit)
                .csrf(AbstractHttpConfigurer::disable)

                // Activation de CORS
                .cors(Customizer.withDefaults())

                .authorizeHttpRequests(auth -> auth
                        // Accès public
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/items/**").permitAll()
                        .requestMatchers("/api/items/get/**").permitAll()
                        .requestMatchers("/api/items/edit/**").permitAll()
                        .requestMatchers("/api/items/delete/**").permitAll()
                        .requestMatchers("/api/items/All").permitAll()
                        .requestMatchers("/api/categories/All").permitAll()

                        .requestMatchers("/commandes/**").permitAll()
                        .requestMatchers("/commandes/add-commandes").permitAll()
                        .requestMatchers("/discounts/**").permitAll()
                        .requestMatchers("/discounts/get-discounts/**").permitAll()
                        .requestMatchers("/discounts/add-discounts/**").permitAll()
                        .requestMatchers("/discounts/itemactive/**").permitAll()
                        .requestMatchers("/api/users/**").permitAll()
                        .requestMatchers("/api/users/image/**").permitAll()

                        .requestMatchers("/api/forgot-password", "/api/reset-password").permitAll()
                        .requestMatchers("/api/test/auth-status").authenticated()
                        .anyRequest().authenticated() // Par défaut, tout le reste nécessite une authentification
                )

                // Configuration de la gestion de la session (stateless)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Ajout du filtre d'autorisation JWT avant le filtre d'authentification par mot de passe
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)

                // Configuration des headers de sécurité
                .headers(headers -> headers
                        .addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsMode.SAMEORIGIN))
                        .contentSecurityPolicy(csp -> csp.policyDirectives("frame-ancestors 'self' http://localhost:4200"))
                        .xssProtection(xss -> xss.headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK))
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Utilisation de BCrypt pour l'encodage des mots de passe
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        // Retourne l'AuthenticationManager pour la gestion des authentifications
        return authenticationConfiguration.getAuthenticationManager();
    }
}
