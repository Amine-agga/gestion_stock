package com.example.ms_gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.config.EnableWebFlux;

@Configuration
@EnableWebFlux
public class GatewayCorsConfiguration {
    @Bean
    public CorsWebFilter corsWebFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfig = new CorsConfiguration();
        // 1. Origine Angular
        corsConfig.addAllowedOrigin("http://localhost:4200");
        // 2. Méthodes (GET, POST, etc., et surtout OPTIONS pour le Pre-flight)
        corsConfig.addAllowedMethod("*");
        // 3. Headers (pour content-type, authorization, etc.)
        corsConfig.addAllowedHeader("*");
        // 4. Important pour les cookies/sessions
        corsConfig.setAllowCredentials(true);
        corsConfig.setMaxAge(3600L);
        // Applique la configuration à toutes les routes (/**)
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }
}