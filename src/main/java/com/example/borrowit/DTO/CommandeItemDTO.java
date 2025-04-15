package com.example.borrowit.DTO;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class CommandeItemDTO {

    private Long id;

    private int quantity;
    private double unitPrice;

}
