package com.example.borrowit.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Date;

public class CommandeDTO {

    private Long id;
    private double totalPrice;
    private Date createdDate;
    private String status;
    private String description;
}
