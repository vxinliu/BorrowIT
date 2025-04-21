package com.example.borrowit.service.impl;

import com.example.borrowit.Entity.User;
import com.example.borrowit.Entity.UserDetailsImpl;
import com.example.borrowit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder(); // Encoder pour les mots de passe
    }

    // 📌 Récupérer tous les utilisateurs
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // 📌 Récupérer un utilisateur par ID
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // 📌 Récupérer un utilisateur par email
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // 📌 Ajouter ou modifier un utilisateur
    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Toujours encoder
        return userRepository.save(user);
    }

    // 📌 Mettre à jour un utilisateur
    public User updateUser(Long id, User updatedUser) {
        return userRepository.findById(id).map(existingUser -> {
            existingUser.setCin(updatedUser.getCin());
            existingUser.setName(updatedUser.getName());
            existingUser.setEmail(updatedUser.getEmail());

            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            }

            existingUser.setPhone(updatedUser.getPhone());
            existingUser.setAddress(updatedUser.getAddress());
            existingUser.setRole(updatedUser.getRole());
            return userRepository.save(existingUser);
        }).orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'ID : " + id));
    }

    // 📌 Supprimer un utilisateur par ID
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    // 📌 Supprimer un utilisateur par email
    public void deleteUserByEmail(String email) {
        userRepository.deleteByEmail(email);
    }

    // ✅ Utilisé par Spring Security pour l’authentification
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return new UserDetailsImpl(user);
    }

    // 📌 Mettre à jour le mot de passe d’un utilisateur
    public void updatePassword(String email, String newPassword) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        } else {
            throw new RuntimeException("Utilisateur non trouvé.");
        }
    }

    // ✅ Créer l'utilisateur si inexistant (appelé par le login Google)
    public void createUserIfNotExists(String email, String name) {
        Optional<User> existing = userRepository.findByEmail(email);
        if (existing.isEmpty()) {
            User user = new User();
            user.setEmail(email);
            user.setName(name);
            user.setPassword(""); // Vide car c'est une connexion via Google
            user.setRole(User.Role.BORROWER); // Tu peux changer le rôle par défaut si tu veux
            userRepository.save(user);
        }
    }
}
