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
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User saveUser(User user) {
        if (user.getPassword() != null && !user.getPassword().isEmpty() && !user.getPassword().startsWith("$2a$")) {
            user.setPassword(passwordEncoder.encode(user.getPassword())); // Encode password only if it is not already encoded
        }
        return userRepository.save(user);
    }

    public User updateUser(Long id, User updatedUser) {
        return userRepository.findById(id).map(existingUser -> {
            existingUser.setCin(updatedUser.getCin());
            existingUser.setName(updatedUser.getName());
            existingUser.setEmail(updatedUser.getEmail());

            // Only encode password if it's not already encoded
            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty() && !updatedUser.getPassword().startsWith("$2a$")) {
                existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            }

            existingUser.setPhone(updatedUser.getPhone());
            existingUser.setAddress(updatedUser.getAddress());
            existingUser.setRole(updatedUser.getRole());
            return userRepository.save(existingUser);
        }).orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'ID : " + id));
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    public void deleteUserByEmail(String email) {
        userRepository.deleteByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable avec l'e-mail : " + email));
        return new UserDetailsImpl(user);
    }

    public void updatePassword(String email, String newPassword) {
        userRepository.findByEmail(email).ifPresentOrElse(user -> {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        }, () -> {
            throw new RuntimeException("Utilisateur non trouvé.");
        });
    }

    public void createUserIfNotExists(String email, String name) {
        if (userRepository.findByEmail(email).isEmpty()) {
            User user = new User();
            user.setEmail(email);
            user.setName(name);
            user.setPassword(""); // No password set initially
            user.setRole(User.Role.BORROWER);
            user.setStatus("Active");
            userRepository.save(user);
        }
    }
}
