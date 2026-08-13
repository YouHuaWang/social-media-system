package com.socialmedia.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {
    private final SecretKey secretKey;
    private final long expiration;

    public JwtService(
        @Value("${jwt.secret}") String secret,
        @Value("${jwt.expiration}") long expiration
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expiration = expiration;
    }

    public String generateToken(Long userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
            .subject(String.valueOf(userId))
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(secretKey)
            .compact();
    }

    public Long getUserId(String token) {
        Claims claims = Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload();

        String subject = claims.getSubject();

        if (subject == null) {
            throw new JwtException("JWT subject 不存在");
        }

        try {
            return Long.valueOf(subject);
        } catch (NumberFormatException e) {
            throw new JwtException("JWT USER_ID 格式錯誤");
        }
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (
            JwtException | 
            IllegalArgumentException e
        ) {
            return false;
        }
    }
}
