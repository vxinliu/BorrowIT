package com.example.borrowit.Dto;

import com.example.borrowit.Entity.StatusItem;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ItemsDTO {
    private Long id;

    private String name;

    private String description;

    private String itemCondition;

    private boolean availability;

    private double price;
    private String image;
    private String categoryType;
    private Long idUser;
    private StatusItem statusItem;

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setItemCondition(String itemCondition) {
        this.itemCondition = itemCondition;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public void setStatusItem(StatusItem statusItem) {
        this.statusItem = statusItem;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getItemCondition() {
        return itemCondition;
    }

    public boolean isAvailability() {
        return availability;
    }

    public double getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public Long getIdUser() {
        return idUser;
    }

    public StatusItem getStatusItem() {
        return statusItem;
    }
}
