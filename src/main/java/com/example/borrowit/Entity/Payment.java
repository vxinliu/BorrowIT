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

    private float amount;
    @JsonProperty("date")

    private Date date;
    @JsonProperty("status")

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @OneToOne(cascade = CascadeType.ALL)
    private Contract contract;

    public enum PaymentStatus {
        PENDING, COMPLETED, FAILED
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
