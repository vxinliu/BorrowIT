package com.example.borrowit.service.impl;

import com.example.borrowit.Entity.User;
import com.example.borrowit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

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

    // 📌 Ajouter un nouvel utilisateur ou modifier un existant
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // 📌 Mettre à jour un utilisateur existant
    public User updateUser(Long id, User updatedUser) {
        return userRepository.findById(id).map(existingUser -> {
            existingUser.setCin(updatedUser.getCin());
            existingUser.setName(updatedUser.getName());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setPassword(updatedUser.getPassword());
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
}
