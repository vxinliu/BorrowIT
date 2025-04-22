package com.example.borrowit.Entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;


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
    private Set<Item> items;

    @OneToMany(mappedBy = "borrower")
    private Set<Request> requests;

    @OneToMany(mappedBy = "user")
    private Set<Review> reviews;



    @OneToMany(mappedBy = "borrower")
    private Set<Contract> borrowedContracts;

    @OneToMany(mappedBy = "owner")
    private Set<Contract> ownedContracts;

    @OneToMany(mappedBy = "user")
    private Set<CustomerService> customerServices;

    @OneToMany(mappedBy = "user")
    private Set<Notification> notifications;

    @OneToMany(mappedBy = "user",cascade=CascadeType.ALL)
    private List<Feedback> feedbacks;

    public enum Role {
ADMIN, BORROWER, OWNER
    }

    public Long getId() {
        return id;
    }

    public Long getCin() {
        return cin;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public Role getRole() {
        return role;
    }

    public Set<Item> getItems() {
        return items;
    }

    public Set<Request> getRequests() {
        return requests;
    }

    public Set<Review> getReviews() {
        return reviews;
    }

    public Set<Contract> getBorrowedContracts() {
        return borrowedContracts;
    }

    public Set<Contract> getOwnedContracts() {
        return ownedContracts;
    }

    public Set<CustomerService> getCustomerServices() {
        return customerServices;
    }

    public Set<Notification> getNotifications() {
        return notifications;
    }
}
