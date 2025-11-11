package com.tricol.Tricol.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // Autorise les cookies/session si besoin (garde true)
        config.setAllowCredentials(true);

        // Autorise ton frontend Angular
        config.addAllowedOrigin("http://localhost:4200");

        // Autorise tous les headers (Content-Type, Authorization, etc.)
        config.addAllowedHeader("*");

        // Autorise toutes les méthodes HTTP (GET, POST, PUT, DELETE...)
        config.addAllowedMethod("*");

        // Applique à toutes les URLs du backend
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}