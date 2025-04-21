package com.example.borrowit.Controller;

import com.example.borrowit.Entity.User;
import com.example.borrowit.service.impl.UserService;
import com.example.borrowit.service.impl.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SmsService smsService;

    // Helper method to generate a 6-digit verification code
    private String generateVerificationCode() {
        Random rand = new Random();
        return String.format("%06d", rand.nextInt(1000000));
    }

    // Get all users
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // Register a new user and send SMS verification
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            // Check if the email is already in use
            if (userService.getUserByEmail(user.getEmail()).isPresent()) {
                return ResponseEntity.badRequest().body("Cet email est déjà utilisé.");
            }

            // Set status to "Pending Verification" and save the user
            user.setStatus("Pending Verification");
            User savedUser = userService.saveUser(user);

            // Generate a verification code
            String verificationCode = generateVerificationCode();

            // Send the verification code via SMS
            smsService.sendSms(user.getPhone(), "Votre code de vérification: " + verificationCode);

            // Save the verification code in the user entity
            savedUser.setVerificationCode(verificationCode);
            userService.saveUser(savedUser);

            // Return the saved user as a response
            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            // Handle any errors that may occur
            return ResponseEntity.badRequest().body("Erreur lors de l'enregistrement: " + e.getMessage());
        }
    }

    // Verify the user with the provided verification code
    @PostMapping("/verify/{userId}")
    public ResponseEntity<?> verifyUser(@PathVariable Long userId, @RequestParam String verificationCode) {
        Optional<User> userOpt = userService.getUserById(userId);
        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = userOpt.get();
        if (verificationCode.equals(user.getVerificationCode())) {
            user.setStatus("Active");
            user.setVerificationCode(null); // Clear the verification code after successful verification
            userService.saveUser(user);
            return ResponseEntity.ok("Compte vérifié et activé avec succès.");
        } else {
            return ResponseEntity.badRequest().body("Code de vérification invalide.");
        }
    }

    // Ban a user
    @PutMapping("/ban/{id}")
    public ResponseEntity<?> banUser(@PathVariable Long id) {
        Optional<User> userOpt = userService.getUserById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setStatus("Banned");
            return ResponseEntity.ok(userService.saveUser(user));
        }
        return ResponseEntity.notFound().build();
    }

    // Update user information
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        Optional<User> userOpt = userService.getUserById(id);
        if (userOpt.isPresent()) {
            User existingUser = userOpt.get();
            existingUser.setName(updatedUser.getName());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setPhone(updatedUser.getPhone());
            existingUser.setAddress(updatedUser.getAddress());
            existingUser.setGenre(updatedUser.getGenre());
            existingUser.setDateDeNaissance(updatedUser.getDateDeNaissance());
            return ResponseEntity.ok(userService.saveUser(existingUser));
        }
        return ResponseEntity.notFound().build();
    }

    // Unban a user
    @PutMapping("/unban/{id}")
    public ResponseEntity<?> unbanUser(@PathVariable Long id) {
        Optional<User> userOpt = userService.getUserById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setStatus("Active");
            return ResponseEntity.ok(userService.saveUser(user));
        }
        return ResponseEntity.notFound().build();
    }

    // Get user by email
    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }
}
