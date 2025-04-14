package com.example.borrowit.Entity;

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
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonProperty("amount")

    private Double amount;
    @JsonProperty("date")

    private Date date;
    @JsonProperty("stripePaymentIntentId")
    private String stripePaymentIntentId; // ID renvoyé par Stripe, utile pour retrouver le paiement
    @JsonProperty("status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @OneToOne(cascade = CascadeType.ALL)
    private Contract contract;



    public enum PaymentStatus {
        PENDING, SUCCEEDED, FAILED,PROCESSING
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Double getAmount() {
        return amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public Contract getContract() {
        return contract;
    }
    public void setStripePaymentIntentId(String stripePaymentIntentId) {
        this.stripePaymentIntentId = stripePaymentIntentId;
    }

}
