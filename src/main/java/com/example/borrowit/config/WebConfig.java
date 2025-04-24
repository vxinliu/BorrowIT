package com.example.borrowit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Autorise toutes les requêtes provenant de http://localhost:4200
        registry.addMapping("/**")  // Autorise toutes les requêtes
                .allowedOrigins("http://localhost:4200")  // Permet les requêtes depuis localhost:4200
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // Autorise les méthodes HTTP spécifiques
                .allowedHeaders("*")  // Permet tous les en-têtes
                .allowCredentials(true);  // Permet l'envoi de cookies ou authentification
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // CORS configuration for specific origin
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:4200")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}
