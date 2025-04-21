package com.example.borrowit.DTO;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class CreateCommandeRequest {
    private Long userId;
    private Long discountId;
    private String description;
    private List<ItemQuantity> items= new ArrayList<>();

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getDiscountId() {
        return discountId;
    }

    public void setDiscountId(Long discountId) {
        this.discountId = discountId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ItemQuantity> getItems() {
        return items;
    }

    public void setItems(List<ItemQuantity> items) {
        this.items = items;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemQuantity {
        private Long itemId;
        private int quantity;


        public Long getItemId() { return itemId; }
        public int getQuantity() { return quantity; }
    }
}
