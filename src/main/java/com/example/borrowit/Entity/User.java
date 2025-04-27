package com.example.borrowit.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.mapping.List;

import java.util.Set;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor


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

    @OneToMany(mappedBy = "owner")
    @JsonIgnore
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

    public Long getCin() {
        return cin;
    }

    public void setCin(Long cin) {
        this.cin = cin;
    }

    public String getName() {
        return name;
    }
    public String getStatus() {
        return status; // CORRECTION: Retourne le champ status, pas name

    }

    public void setStatus(String status) {
        this.status = status;
    }
    public void setName(String name) {
        this.name = name;
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

    public Set<Item> getItems() {
        return items;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }

    public Set<Request> getRequests() {
        return requests;
    }

    public void setRequests(Set<Request> requests) {
        this.requests = requests;
    }

    public Set<Review> getReviews() {
        return reviews;
    }

    public void setReviews(Set<Review> reviews) {
        this.reviews = reviews;
    }





    public Set<Contract> getBorrowedContracts() {
        return borrowedContracts;
    }

    public void setBorrowedContracts(Set<Contract> borrowedContracts) {
        this.borrowedContracts = borrowedContracts;
    }

    public Set<Contract> getOwnedContracts() {
        return ownedContracts;
    }

    public void setOwnedContracts(Set<Contract> ownedContracts) {
        this.ownedContracts = ownedContracts;
    }

    public Set<CustomerService> getCustomerServices() {
        return customerServices;
    }

    public void setCustomerServices(Set<CustomerService> customerServices) {
        this.customerServices = customerServices;
    }

    public Set<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(Set<Notification> notifications) {
        this.notifications = notifications;
    }
}
