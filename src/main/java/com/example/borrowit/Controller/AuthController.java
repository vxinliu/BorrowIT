package com.example.borrowit.controller;
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

            // Authenticate user without hashing password (plain text)
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generate JWT token
            UserDetails userDetails = userServices.loadUserByUsername(user.getEmail());
            String token = jwtUtil.generateToken(userDetails);

            // Create and return JwtResponse with the generated token
            JwtResponse jwtResponse = new JwtResponse(token);
            logger.info("Authentication successful for user: {}", user.getEmail());
            return ResponseEntity.ok(jwtResponse);
        } catch (BadCredentialsException e) {
            // Handle invalid credentials
            logger.error("Authentication failed for user: {}. Reason: Invalid email or password.", user.getEmail(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password.");
        } catch (Exception e) {
            // Handle other exceptions
            logger.error("Authentication failed for user: {}. Reason: {}", user.getEmail(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed.");
        }
    }


}
