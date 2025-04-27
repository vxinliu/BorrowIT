package com.example.borrowit.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Commande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //@JsonProperty
    private double totalPrice;
    //@JsonProperty
    private Date createdDate;
    //@JsonProperty
    private String status;
    //@JsonProperty
    private String description;
@OneToOne
@JsonProperty("item")
private Item item;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Item getItem() {
        return item;
    }


//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "discount_id")
//    //@JsonBackReference
//    private Discount discount;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;




}



