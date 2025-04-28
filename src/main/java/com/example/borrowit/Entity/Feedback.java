package com.example.borrowit.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;

    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "item_id")  // Optionnel, si tu veux une colonne explicite pour Item
    private Item item;

    @ManyToOne
    @JoinColumn(name = "user_id")  // Ajout du mappage de la relation avec User
    private User user;

    @OneToMany(mappedBy = "feedback")
    private Set<Reacts> reacts;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Set<Reacts> getReacts() {
        return reacts;
    }

    public void setReacts(Set<Reacts> reacts) {
        this.reacts = reacts;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
