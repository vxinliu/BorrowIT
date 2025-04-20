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
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @JsonProperty("image")
    private String image;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;
    //@Enumerated(EnumType.STRING)
   // @JsonProperty("categoryType")
    //private  CategoryType categoryType;

    @OneToMany(mappedBy = "category")
    @JsonIgnore // Ignorer cette relation dans la sérialisation pour éviter la boucle infinie
    private Set<Item> items;



}
