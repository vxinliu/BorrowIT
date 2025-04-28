package com.example.borrowit.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Reclamation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String issueDescription;

    @Enumerated(EnumType.STRING)
    private ServiceStatus status;

    @ManyToOne(optional = true)
    @JoinColumn(name = "delivery_id", nullable = true)
    @JsonIgnore
    private Delivery delivery;

    @ManyToOne
    @JsonIgnore
    private User user;
    private String response;
    private LocalDate createdAt;
    private LocalDate respondedAt;

    @ElementCollection
    @CollectionTable(name = "reclamation_images", joinColumns = @JoinColumn(name = "reclamation_id"))
    @Column(name = "image_bytes", columnDefinition = "LONGBLOB")
    private List<byte[]> imageBytes = new ArrayList<>();


    public List<byte[]> getImageBytes() {
        return imageBytes;
    }
    public void setImageBytes(List<byte[]> imageBytes) {
        this.imageBytes = imageBytes;
    }

    @PrePersist
    public void onCreate() {
        if (this.status == null) {
            this.status = ServiceStatus.OPEN;
        }
    }
}