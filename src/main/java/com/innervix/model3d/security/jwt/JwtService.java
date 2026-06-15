package com.innervix.model3d.security.jwt;

import com.innervix.model3d.config.AppProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {

    private final AppProperties properties;

    public JwtService(AppProperties properties) {
        this.properties = properties;
    }

    public String createAdminToken(String username) {
        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(properties.getSecurity().getTokenValiditySeconds());
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", List.of("ROLE_ADMIN"))
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiresAt))
                .signWith(signingKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims parse(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key signingKey() {
        String secret = properties.getSecurity().getJwtSecret();
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}
