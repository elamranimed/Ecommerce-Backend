package com.example.EcommerceBackend.Security;

import com.example.EcommerceBackend.Entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    
    @Value("${jwt.secret:VotreClefSecreteTresLongueEtSecuriseePourJWTTokenGeneration}")
    private String secret;
    
    @Value("${jwt.expiration:86400000}")
    private Long expiration;
    
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
    
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("role", user.getRole().toString());
        claims.put("username", user.getUsername()); 
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }
    
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }
    
    public Long extractUserId(String token) {
        Object idObj = extractClaims(token).get("id");
        if (idObj == null) return null;
        if (idObj instanceof Integer) return ((Integer) idObj).longValue();
        if (idObj instanceof Long) return (Long) idObj;
        return Long.parseLong(idObj.toString());
    }
    
    public String extractRole(String token) {
        Object role = extractClaims(token).get("role");
        return role != null ? role.toString() : null;
    }
    
    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    
    public boolean validateToken(String token) {
        try {
            extractClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean isTokenExpired(String token) {
        try {
            Date exp = extractClaims(token).getExpiration();
            return exp == null || exp.before(new Date());
        } catch (Exception e) {
            return true;
        }
    }
}