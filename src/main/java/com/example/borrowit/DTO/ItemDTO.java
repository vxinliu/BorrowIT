package com.example.borrowit.DTO;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class ItemDTO {

    private double price;
    private String name;
    private String description;
    private String itemCondition;
    private boolean availability;
}
