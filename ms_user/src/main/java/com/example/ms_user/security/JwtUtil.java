package com.example.ms_user.security;

import com.example.ms_user.model.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String SECRET_KEY;
    @Value("${jwt.expiration}")
    private Long EXPIRATION;
    public String generateToken(String email, Role role, String nom, String prenom){
        Map<String,Object> claims = new HashMap<>();
        claims.put("role",role.name());
        claims.put("nom",nom);
        claims.put("prenom",prenom);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(SignatureAlgorithm.HS256,SECRET_KEY)
                .compact();
    }
    private Claims extractAllClaims(String token){
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
    public String extractEmail(String token){
        return extractAllClaims(token).getSubject();
    }
    public String extarctNom(String token){
        return extractAllClaims(token).get("nom",String.class);
    }
    public String extractPrenom(String token){
        return extractAllClaims(token).get("prenom", String.class);
    }
    public Role extractRole(String token){
        return extractAllClaims(token).get("role", Role.class);
    }
    public boolean isTokenExpired(String token){
        return extractAllClaims(token).getExpiration().before(new Date());
    }
    public boolean validateToken(String token){
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return !isTokenExpired(token);
        }catch(Exception ex){
            return false;
        }
    }
}
