package com.example.borrowit.Controller;

import com.example.borrowit.Entity.User;
import com.example.borrowit.service.impl.UserService;
import com.example.borrowit.JwtUtil;
import com.example.borrowit.JwtResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authenticationManager;
    private final UserService userServices;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                          UserService userServices,
                          JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userServices = userServices;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            logger.info("Attempting to authenticate user with email: {}", user.getEmail());
            logger.debug("Password received: {}", user.getPassword()); // Log du mot de passe reçu

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetails userDetails = userServices.loadUserByUsername(user.getEmail());
            String token = jwtUtil.generateToken(userDetails);

            JwtResponse jwtResponse = new JwtResponse(token);
            logger.info("Authentication successful for user: {}", user.getEmail());
            return ResponseEntity.ok(jwtResponse);
        } catch (BadCredentialsException e) {
            logger.error("Authentication failed for user: {}. Reason: Invalid email or password", user.getEmail());
            logger.error("Stack trace:", e); // Séparé pour éviter l'ambiguïté
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password.");
        } catch (Exception e) {
            logger.error("Authentication failed for user: {}. Reason: {}", user.getEmail(), e.getMessage());
            logger.error("Stack trace:", e); // Séparé pour éviter l'ambiguïté
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed.");
        }
    }

    @PostMapping("/login/google")
    public ResponseEntity<?> googleLogin(@RequestBody OAuth2User oauth2User) {
        String email = oauth2User.getAttribute("email");
        try {
            logger.info("Attempting to authenticate user with Google email: {}", email);

            UserDetails userDetails = userServices.loadUserByUsername(email);
            if (userDetails == null) {
                logger.warn("User with email {} does not exist. Creating new user.", email);
                // Logique de création d'utilisateur si nécessaire
            }

            String token = jwtUtil.generateToken(userDetails);

            JwtResponse jwtResponse = new JwtResponse(token);
            logger.info("Authentication successful for Google user: {}", email);
            return ResponseEntity.ok(jwtResponse);

        } catch (OAuth2AuthenticationException e) {
            logger.error("Authentication failed for Google user: {}. Reason: {}", email, e.getMessage());
            logger.error("Stack trace:", e); // Séparé pour éviter l'ambiguïté
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Google authentication failed.");
        } catch (Exception e) {
            logger.error("Authentication failed for Google user: {}. Reason: {}", email, e.getMessage());
            logger.error("Stack trace:", e); // Séparé pour éviter l'ambiguïté
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed.");
        }
    }
}