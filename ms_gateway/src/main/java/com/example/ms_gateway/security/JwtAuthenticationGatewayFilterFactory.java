package com.example.ms_gateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationGatewayFilterFactory extends AbstractGatewayFilterFactory<JwtAuthenticationGatewayFilterFactory.Config> {

    @Autowired
    private JwtUtil jwtUtil;

    public JwtAuthenticationGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            String authHeader = exchange.getRequest()
                    .getHeaders()
                    .getFirst(HttpHeaders.AUTHORIZATION);

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return onError(exchange, "Missing or invalid Authorization header");
            }

            String token = authHeader.substring(7);

            try {
                if (!jwtUtil.validateToken(token)) {
                    return onError(exchange, "Invalid or expired JWT token");
                }
            } catch (Exception ex) {
                return onError(exchange, "JWT validation error: " + ex.getMessage());
            }

            String email = jwtUtil.extractEmail(token);
            String role = jwtUtil.extractRole(token);

            ServerWebExchange modifiedExchange = exchange.mutate()
                    .request(r -> r.header("X-User-Email", email)
                            .header("X-User-Role", role))
                    .build();

            System.out.println("✅ JWT validé - Email: " + email + ", Role: " + role);

            return chain.filter(modifiedExchange);
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String message) {
        exchange.getResponse().setRawStatusCode(401);
        System.out.println("❌ JWT Error: " + message);
        return exchange.getResponse().setComplete();
    }

    public static class Config {
        // Configuration vide
    }
}