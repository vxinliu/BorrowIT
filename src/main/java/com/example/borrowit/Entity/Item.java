package com.example.borrowit.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Item implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String itemCondition;
    private boolean availability;

    @ManyToOne
    private User owner;

    public Long getId() {
        return id;
    }

    @ManyToOne
    private Category category;

    @OneToMany(mappedBy = "item",cascade=CascadeType.ALL)
    private List<Feedback> feedbacks;
}
