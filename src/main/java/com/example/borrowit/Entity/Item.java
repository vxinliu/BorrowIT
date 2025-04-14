package com.example.borrowit.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    private Double price;
    @JsonProperty("owner")

    @ManyToOne
    private User owner;
    @JsonProperty("category")

    @ManyToOne
    private Category category;

   @OneToMany
   private Set<Feedback> feedbacks;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getItemCondition() {
        return itemCondition;
    }

    public boolean isAvailability() {
        return availability;
    }

    public Double getPrice() {
        return price;
    }

    public User getOwner() {
        return owner;
    }
}
