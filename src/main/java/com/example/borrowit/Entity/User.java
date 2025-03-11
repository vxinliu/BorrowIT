package com.example.borrowit.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.mapping.List;

import java.util.Set;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonProperty
    private Long cin;
    @JsonProperty
    private String name;
    @JsonProperty
    private String email;
    @JsonProperty
    private String password;
    @JsonProperty
    private String phone;
    @JsonProperty
    private String address;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "owner")
    private Set<Item> items;

    @OneToMany(mappedBy = "borrower")
    private Set<Request> requests;

    @OneToMany(mappedBy = "user")
    private Set<Review> reviews;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Commande> commandes;

    @OneToMany(mappedBy = "borrower")
    private Set<Contract> borrowedContracts;

    @OneToMany(mappedBy = "owner")
    private Set<Contract> ownedContracts;

    @OneToMany(mappedBy = "user")
    private Set<CustomerService> customerServices;

    @OneToMany(mappedBy = "user")
    private Set<Notification> notifications;

    public enum Role {
ADMIN, BORROWER, OWNER
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
