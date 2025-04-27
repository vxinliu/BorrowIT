package com.example.borrowit.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonProperty("name")

    private String name;


    @JsonProperty("description")
    private String description;

    @JsonProperty("itemCondition")
    private String itemCondition;

    @JsonProperty("availability")
    private boolean availability;

    /*
    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    @JsonBackReference(value = "item-commande")
    private Set<Commande> commandes;

    public Set<Commande> getCommandes() {
        return commandes;
    }

    public void setCommandes(Set<Commande> commandes) {
        this.commandes = commandes;
    }
*/
    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "item-discount")
    private Set<Discount> discounts;

    public Set<Discount> getDiscounts() {
        return discounts;
    }

    public void setDiscounts(Set<Discount> discounts) {
        this.discounts = discounts;
    }

    public double getPrice() {
        return price;
    }


    public Set<Feedback> getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(Set<Feedback> feedbacks) {
        this.feedbacks = feedbacks;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public String getItemCondition() {
        return itemCondition;
    }

    public void setItemCondition(String itemCondition) {
        this.itemCondition = itemCondition;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonProperty("price")
    private double price; // Ajout du prix

    @Lob
    @JsonProperty("image")
    private String image; // Ajout de l'image

    @ManyToOne
    @JsonIgnoreProperties("items") // Ignorer les items de l’utilisateur pour éviter la récursivité
    @JsonIgnore // Ignorer la sérialisation de la propriété "owner"
    private User owner;


    @ManyToOne
    @JsonBackReference(value = "item-category")
    private Category category;

    @OneToMany
    @JsonIgnore // Ignorer la sérialisation de la propriété "feedbacks"
    private Set<Feedback> feedbacks;


    public boolean getAv() {
        return this.availability;
    }
}
