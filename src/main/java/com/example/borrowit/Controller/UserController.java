package com.example.borrowit.controller;

import com.example.borrowit.Entity.User;
import com.example.borrowit.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            User savedUser = userService.saveUser(user);
            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de l'enregistrement : " + e.getMessage());
        }
    }

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
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        Optional<User> userOpt = userService.getUserById(id);
        if (userOpt.isPresent()) {
            User existingUser = userOpt.get();

            // Mettre à jour les informations de l'utilisateur
            existingUser.setName(updatedUser.getName());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setPhone(updatedUser.getPhone());
            existingUser.setAddress(updatedUser.getAddress());
            existingUser.setGenre(updatedUser.getGenre());
            existingUser.setDateDeNaissance(updatedUser.getDateDeNaissance());

            // Sauvegarder l'utilisateur mis à jour
            userService.saveUser(existingUser);

            return ResponseEntity.ok(existingUser);
        }
        return ResponseEntity.notFound().build();
    }


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

    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        Optional<User> userOpt = userService.getUserById(id);
        if (userOpt.isPresent()) {
            userService.deleteUserById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
