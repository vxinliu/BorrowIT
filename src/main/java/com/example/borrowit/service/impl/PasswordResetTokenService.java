package com.example.borrowit.service.impl;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class PasswordResetTokenService {

    @Value("${jwt.reset.secret}")
    private String secretKeyString;

    @Value("${jwt.reset.expiration}")
    private int tokenValidity;

    public String generateToken(String email) {
        SecretKey secretKey = new SecretKeySpec(
                secretKeyString.getBytes(StandardCharsets.UTF_8),
                SignatureAlgorithm.HS256.getJcaName()
        );

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + tokenValidity))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String validateToken(String token) throws JwtException {
        SecretKey secretKey = new SecretKeySpec(
                secretKeyString.getBytes(StandardCharsets.UTF_8),
                SignatureAlgorithm.HS256.getJcaName()
        );

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        if (claims.getExpiration().before(new Date())) {
            throw new ExpiredJwtException(null, claims, "Token expired");
        }

        return claims.getSubject();
    }
}