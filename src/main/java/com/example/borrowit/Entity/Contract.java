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

    @ManyToOne
    private User borrower;

    @ManyToOne
    private User owner;

    @OneToOne
    private Payment payment;

    public void setBorrower(User borrower) {
        this.borrower = borrower;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
