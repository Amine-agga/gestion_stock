package com.example.ms_gateway.security;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Component
public class RequireRoleGatewayFilterFactory extends AbstractGatewayFilterFactory<RequireRoleGatewayFilterFactory.Config> {

    public RequireRoleGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            // Récupérer le rôle ajouté par JwtAuthenticationGatewayFilterFactory
            String userRole = exchange.getRequest().getHeaders().getFirst("X-User-Role");

            if (userRole == null) {
                return onError(exchange, "Missing user role", HttpStatus.FORBIDDEN);
            }

            // Vérifier si le rôle est dans la liste des rôles autorisés
            List<String> allowedRoles = Arrays.asList(config.getRoles().split(","));

            if (!allowedRoles.contains(userRole)) {
                System.out.println("❌ Access denied - Required: " + config.getRoles() + ", Got: " + userRole);
                return onError(exchange, "Insufficient permissions", HttpStatus.FORBIDDEN);
            }

            System.out.println("✅ Role validated - User: " + userRole);
            return chain.filter(exchange);
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String message, HttpStatus status) {
        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().add("X-Error-Message", message);
        System.out.println("❌ Role Error: " + message);
        return exchange.getResponse().setComplete();
    }

    public static class Config {
        private String roles;

        public String getRoles() {
            return roles;
        }

        public void setRoles(String roles) {
            this.roles = roles;
        }
    }
}