package com.example.borrowit.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Entity
@Data
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Lob
    private byte[] image;

    private String name;
    private String description;

    @OneToMany(mappedBy = "category")
    @JsonManagedReference
    private Set<Item> items;
public enum CategoryType {
        ELECTRONICS, FURNITURE, CLOTHING, BOOKS, OTHER
    }
}
