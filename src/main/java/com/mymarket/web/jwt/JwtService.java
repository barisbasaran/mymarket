package com.mymarket.web.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
@Slf4j
public class JwtService {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    public String generateToken(String username) {
        return buildToken(username);
    }

    public boolean isTokenValid(String token) {
        try {
            var claims = extractClaims(token);
            return !claims.getExpiration().before(new Date());
        } catch (Exception ex) {
            log.error("Error while validating token {}", ex.getMessage());
            return false;
        }
    }

    public String getUserName(String token) {
        return extractClaims(token).getSubject();
    }

    private String buildToken(String username) {
        return Jwts.builder()
            .subject((username))
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
            .signWith(getSignInKey())
            .compact();
    }

    private Claims extractClaims(String token) {
        return Jwts
            .parser()
            .verifyWith(getSignInKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = secretKey.getBytes(UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
