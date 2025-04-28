package com.example.borrowit.DTO;

public class FeedbackRequestDTO {
    private Long id;
    private String message;
    private Long userId;

    public Long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public Long getUserId() {
        return userId;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
