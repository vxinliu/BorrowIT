package com.example.borrowit.DTO;

public class UserDTO {
    private Long id;
    private double price;
    private String name;
    private String description;
    private String itemCondition;
    private boolean availability;
    public enum Role {
        ADMIN, BORROWER, OWNER
    }
}
