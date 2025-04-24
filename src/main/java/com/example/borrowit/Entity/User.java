package com.example.borrowit.Entity;

import jakarta.persistence.*;

import java.util.List;
import java.util.Set;

@Entity
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
    private String genre;  // Champ genre ajouté
    @Column(nullable = false, columnDefinition = "VARCHAR(20) DEFAULT 'Active'")
    private String status = "Active";
    private String dateDeNaissance;  // Champ dateDeNaissance ajouté

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Feedback> feedbacks;

    @OneToMany(mappedBy = "owner")
    private List<Item> items;

    @OneToMany(mappedBy = "borrower")
    private List<Request> requests;

    @OneToMany(mappedBy = "user")
    private List<Review> reviews;

    @OneToMany(mappedBy = "user")
    private List<Payment> payments;


    @OneToMany(mappedBy = "borrower")
    private List<Contract> borrowedContracts;

    @OneToMany(mappedBy = "owner")
    private List<Contract> ownedContracts;

    @OneToMany(mappedBy = "user")
    private List<CustomerService> customerServices;

    @OneToMany(mappedBy = "user")
    private List<Notification> notifications;

    // Enum Role pour définir les rôles de l'utilisateur
    public enum Role {
        ADMIN, BORROWER, OWNER
    }

    // Getters et Setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCin() {
        return cin;
    }

    public void setCin(Long cin) {
        this.cin = cin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDateDeNaissance() {
        return dateDeNaissance;
    }

    public void setDateDeNaissance(String dateDeNaissance) {
        this.dateDeNaissance = dateDeNaissance;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Feedback> getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(List<Feedback> feedbacks) {
        this.feedbacks = feedbacks;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public List<Request> getRequests() {
        return requests;
    }

    public void setRequests(List<Request> requests) {
        this.requests = requests;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public List<Contract> getBorrowedContracts() {
        return borrowedContracts;
    }

    public void setBorrowedContracts(List<Contract> borrowedContracts) {
        this.borrowedContracts = borrowedContracts;
    }

    public List<Contract> getOwnedContracts() {
        return ownedContracts;
    }

    public void setOwnedContracts(List<Contract> ownedContracts) {
        this.ownedContracts = ownedContracts;
    }

    public List<CustomerService> getCustomerServices() {
        return customerServices;
    }

    public void setCustomerServices(List<CustomerService> customerServices) {
        this.customerServices = customerServices;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }
}
