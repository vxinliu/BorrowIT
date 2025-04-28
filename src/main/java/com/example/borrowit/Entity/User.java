package com.example.borrowit.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    private Long cin;
    private String name;
    private String email;
    private String password;
    private String phone;
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
    private Set<Review> reviews;



    @OneToMany(mappedBy = "borrower")
    private Set<Contract> borrowedContracts;

    @OneToMany(mappedBy = "owner")
    private Set<Contract> ownedContracts;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Set<Reclamation> reclamations;

    @OneToMany(mappedBy = "user")
    private Set<Notification> notifications;

    public enum Role {
        ADMIN, BORROWER, OWNER
    }
}
