package com.example.borrowit.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @JsonIgnore
    private Set<Item> items;

    @OneToMany(mappedBy = "borrower")
    @JsonIgnore
    private Set<Request> requests;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Set<Review> reviews;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Commande> commandes;

    @OneToMany(mappedBy = "borrower")
    @JsonIgnore
    private Set<Contract> borrowedContracts;

    @OneToMany(mappedBy = "owner")
    @JsonIgnore
    private Set<Contract> ownedContracts;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Set<CustomerService> customerServices;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
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
