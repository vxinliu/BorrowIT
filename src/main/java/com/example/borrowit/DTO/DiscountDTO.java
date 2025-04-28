package com.example.borrowit.DTO;

import com.example.borrowit.Entity.Discount;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class DiscountDTO {
    private Long id;
    private String name;
    private String code;
    private float percentage;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean active;
    private Long item_id;
    private List<CommandeDTO> commandes;

    public List<CommandeDTO> getCommandes() {
        return commandes;
    }

    public void setCommandes(List<CommandeDTO> commandes) {
        this.commandes = commandes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public float getPercentage() {
        return percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Long getItem_id() {
        return item_id;
    }

    public void setItem_id(Long item_id) {
        this.item_id = item_id;
    }


}
