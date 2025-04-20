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
}
