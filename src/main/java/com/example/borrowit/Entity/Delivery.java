package com.example.borrowit.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Delivery implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String address;
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;
    private LocalDate deliveryDate;
    private LocalDate createdAt;

    @OneToOne
    @JsonBackReference
    private Request request;

    @OneToMany(mappedBy = "delivery", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Reclamation>  reclamations;


    @PrePersist
    protected void onCreate() {
        createdAt = LocalDate.now();
        status = DeliveryStatus.PENDING;
    }
}
