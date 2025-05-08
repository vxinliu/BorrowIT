package com.example.borrowit.Dto;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class CommandeRequest {
    private Long itemId;
    private Long userId;
    private String description;



    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
