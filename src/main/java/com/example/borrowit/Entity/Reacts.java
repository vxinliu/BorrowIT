package com.example.borrowit.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Reacts implements Serializable {
    @Serial
    private static final long serialVersionUID=1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Reaction reaction;

    private Date date;

    @ManyToOne
    private User user;


    @ManyToOne
    @JsonBackReference
    private Feedback feedback;

    public enum Reaction {
        LIKE, DISLIKE, LOVE, LAUGH, SAD, ANGRY
    }
}
