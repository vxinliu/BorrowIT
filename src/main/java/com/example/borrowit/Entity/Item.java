package com.example.borrowit.Entity;

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

    @JsonProperty("price")
    private double price; // Ajout du prix

    @Lob
    @JsonProperty("image")
    private String image; // Ajout de l'image

    @ManyToOne
    @JsonIgnore // Ignorer la sérialisation de la propriété "owner"
    private User owner;

    @ManyToOne
    private Category category;

    @OneToMany
    @JsonIgnore // Ignorer la sérialisation de la propriété "feedbacks"
    private Set<Feedback> feedbacks;


    public boolean getAv() {
        return this.availability;
    }
}
