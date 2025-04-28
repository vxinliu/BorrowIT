package com.example.borrowit.Controller;

import com.example.borrowit.Entity.User;
import com.example.borrowit.service.impl.SmsService;
import com.example.borrowit.service.impl.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class UserController {

    private final UserService userService;
    private final SmsService smsService;

    @Autowired
    public UserController(UserService userService, SmsService smsService) {
        this.userService = userService;
        this.smsService = smsService;
    }

    private String generateVerificationCode() {
        return String.format("%06d", new Random().nextInt(1000000));
    }

    // 📌 Get all users
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // 📌 Get user image by email
    @GetMapping("/image/{email}")
    public ResponseEntity<String> getUserImageByEmail(@PathVariable String email) {
        Optional<User> userOpt = userService.getUserByEmail(email);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            byte[] imageBytes = user.getImage();
            if (imageBytes != null) {
                String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                return ResponseEntity.ok(base64Image);
            } else {
                return ResponseEntity.notFound().build();
            }
        }
        return ResponseEntity.notFound().build();
    }

    // 📌 Register user with image upload
    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> registerUser(
            @RequestPart("user") String userJson,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            User user = objectMapper.readValue(userJson, User.class);

            if (userService.getUserByEmail(user.getEmail()).isPresent()) {
                return ResponseEntity.badRequest().body("Cet email est déjà utilisé.");
            }

            if (image != null && !image.isEmpty()) {
                user.setImage(image.getBytes());
            }

            user.setStatus("Pending Verification");
            String verificationCode = generateVerificationCode();
            user.setVerificationCode(verificationCode);

            User savedUser = userService.saveUser(user);
            smsService.sendSms(user.getPhone(), "Votre code de vérification: " + verificationCode);

            return ResponseEntity.ok(savedUser);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Erreur lors du traitement des données.");
        }
    }

    // 📌 Verify user
    @PostMapping("/verify/{userId}")
    public ResponseEntity<?> verifyUser(@PathVariable Long userId, @RequestParam String verificationCode) {
        Optional<User> userOpt = userService.getUserById(userId);
        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = userOpt.get();
        if (verificationCode.equals(user.getVerificationCode())) {
            user.setStatus("Active");
            user.setVerificationCode(null);
            userService.saveUser(user);
            return ResponseEntity.ok("Compte vérifié et activé avec succès.");
        } else {
            return ResponseEntity.badRequest().body("Code de vérification invalide.");
        }
    }

    // 📌 Ban user
    @PutMapping("/ban/{id}")
    public ResponseEntity<?> banUser(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(user -> {
                    user.setStatus("Banned");
                    return ResponseEntity.ok(userService.saveUser(user));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // 📌 Unban user
    @PutMapping("/unban/{id}")
    public ResponseEntity<?> unbanUser(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(user -> {
                    user.setStatus("Active");
                    return ResponseEntity.ok(userService.saveUser(user));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // 📌 Update user with image
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateUser(
            @PathVariable Long id,
            @RequestPart("user") String userJson,
            @RequestPart(value = "image", required = false) MultipartFile imageFile) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            User updatedUser = objectMapper.readValue(userJson, User.class);

            return userService.getUserById(id)
                    .map(user -> {
                        user.setName(updatedUser.getName());
                        user.setEmail(updatedUser.getEmail());
                        user.setPhone(updatedUser.getPhone());
                        user.setAddress(updatedUser.getAddress());
                        user.setGenre(updatedUser.getGenre());
                        user.setDateDeNaissance(updatedUser.getDateDeNaissance());

                        try {
                            if (imageFile != null && !imageFile.isEmpty()) {
                                user.setImage(imageFile.getBytes());
                            }
                            return ResponseEntity.ok(userService.saveUser(user));
                        } catch (IOException e) {
                            return ResponseEntity.status(500).body("Erreur lors du traitement de l'image.");
                        }
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Erreur de format JSON.");
        }
    }

    // 📌 Get user by email
    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 📌 Delete user by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        Optional<User> userOpt = userService.getUserById(id);
        if (userOpt.isPresent()) {
            userService.deleteUserById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // 📌 Delete user by email
    @DeleteMapping("/email/{email}")
    public ResponseEntity<Void> deleteUserByEmail(@PathVariable String email) {
        if (userService.getUserByEmail(email).isPresent()) {
            userService.deleteUserByEmail(email);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
