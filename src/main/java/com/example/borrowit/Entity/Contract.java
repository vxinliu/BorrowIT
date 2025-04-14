package com.example.borrowit.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonProperty("startDate")

    private Date startDate;
    @JsonProperty("endDate")

    private Date endDate;
    @JsonProperty("terms")
    private String terms;
    @JsonProperty("details")
    private String details;
    @ManyToOne
    private User borrower;

    @ManyToOne
    private User owner;

  @OneToOne
private Payment payment;
@Column(name = "owner_signature", columnDefinition = "TEXT")
    private String ownerSignature;
    @Column(name = "borrower_signature", columnDefinition = "TEXT")
    private String borrowerSignature;

    public void setBorrower(User borrower) {
        this.borrower = borrower;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

//    public void setPayment(Payment payment) {
//        this.payment = payment;
//    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOwnerSignature() {
        return ownerSignature;
    }

    public void setOwnerSignature(String ownerSignature) {
        this.ownerSignature = ownerSignature;
    }

    public String getBorrowerSignature() {
        return borrowerSignature;
    }

    public void setBorrowerSignature(String borrowerSignature) {
        this.borrowerSignature = borrowerSignature;
    }

    public Long getId() {
        return id;
    }
}
