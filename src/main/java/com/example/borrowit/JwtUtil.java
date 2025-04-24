package com.example.borrowit;

import com.example.borrowit.Entity.UserDetailsImpl;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    // Clé secrète utilisée pour signer et valider le token
    private static final String SECRET_KEY_STRING = "your-secret-key-that-is-at-least-32-characters-long";
    private static final SecretKey SECRET_KEY = new SecretKeySpec(
            SECRET_KEY_STRING.getBytes(StandardCharsets.UTF_8),
            SignatureAlgorithm.HS256.getJcaName());

    // Durée de validité du token (10 heures ici)
    private static final int TOKEN_VALIDITY = 1000 * 60 * 60 * 10; // 10 heures

    // Logger pour les erreurs et événements
    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    // Extraire le nom d'utilisateur (email) du token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extraire la date d'expiration du token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Extraire une réclamation spécifique du token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token).getBody();
        return claimsResolver.apply(claims);
    }

    // Analyser toutes les réclamations du token
    public Jws<Claims> extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            logger.error("Token has expired.");
            throw new RuntimeException("Le token JWT a expiré", e);
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature.");
            throw new RuntimeException("Signature JWT invalide", e);
        } catch (Exception e) {
            logger.error("Invalid JWT token.");
            throw new RuntimeException("Token JWT invalide", e);
        }
    }

    // Vérifier si le token est expiré
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Générer un token avec des revendications et un sujet (email)
    public String generateToken(UserDetails userDetails) {
        if (!(userDetails instanceof UserDetailsImpl)) {
            throw new IllegalArgumentException("UserDetails must be of type UserDetailsImpl");
        }

        UserDetailsImpl userDetailsImpl = (UserDetailsImpl) userDetails;

        Map<String, Object> claims = new HashMap<>();
        claims.put("name", userDetailsImpl.getName());
        claims.put("genre", userDetailsImpl.getGenre());
        claims.put("dateDeNaissance", userDetailsImpl.getDateDeNaissance().toString());
        claims.put("role", userDetailsImpl.getRole());

        return createToken(claims, userDetails.getUsername());
    }

    // Créer un token avec des revendications, un sujet, une date d'émission et une date d'expiration
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    // Valider le token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
