package com.example.borrowit.Entity;

import com.example.borrowit.Entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class UserDetailsImpl implements UserDetails {

    private final User user;

    public UserDetailsImpl(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Convertir le rôle de l'utilisateur en une autorité Spring Security
        return Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail(); // Utiliser l'email comme nom d'utilisateur
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Le compte n'expire jamais
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Le compte n'est jamais verrouillé
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Les informations d'identification n'expirent jamais
    }

    @Override
    public boolean isEnabled() {
        return true; // Le compte est toujours activé
    }

    // Ajoutez des méthodes supplémentaires pour accéder aux propriétés de l'utilisateur
    public String getName() {
        return user.getName();
    }



    public String getGenre() {
        return user.getGenre(); // Assurez-vous que cette méthode existe dans votre entité User
    }

    public String getDateDeNaissance() {
        return user.getDateDeNaissance(); // Assurez-vous que cette méthode existe dans votre entité User
    }

    public String getRole() {
        return user.getRole().name();
    }
}