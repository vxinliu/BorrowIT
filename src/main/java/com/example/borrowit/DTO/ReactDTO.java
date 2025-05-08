package com.example.borrowit.Dto;

import com.example.borrowit.Entity.Reacts;
import com.example.borrowit.Entity.User;
import java.util.Base64;
import java.util.Date;

public class ReactDTO {
    private Long id;
    private String reaction;
    private Date date;

    // User information
    private Long userId;
    private String userName;
    private String userEmail;
    private String userPhone;
    private String userAvatar;
    private String userGenre;
    private Long userCin;

    private Long feedbackId;

    public ReactDTO(Reacts react) {
        if (react == null) {
            throw new IllegalArgumentException("Reacts object cannot be null");
        }

        this.id = react.getId();
        this.reaction = react.getReaction() != null ? react.getReaction().name() : null;
        this.date = react.getDate();
        this.feedbackId = react.getFeedback() != null ? react.getFeedback().getId() : null;

        User user = react.getUser();
        if (user != null) {
            this.userId = user.getId();
            this.userName = user.getName();
            this.userEmail = user.getEmail();
            this.userPhone = user.getPhone();
            this.userCin = user.getCin();
            this.userGenre = user.getGenre();

            // Handle avatar with proper Base64 encoding
            byte[] imageData = user.getImage();
            if (imageData != null && imageData.length > 0) {
                this.userAvatar = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(imageData);
            }
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReaction() {
        return reaction;
    }

    public void setReaction(String reaction) {
        this.reaction = reaction;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getUserGenre() {
        return userGenre;
    }

    public void setUserGenre(String userGenre) {
        this.userGenre = userGenre;
    }

    public Long getUserCin() {
        return userCin;
    }

    public void setUserCin(Long userCin) {
        this.userCin = userCin;
    }

    public Long getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(Long feedbackId) {
        this.feedbackId = feedbackId;
    }

    @Override
    public String toString() {
        return "ReactDTO{" +
                "id=" + id +
                ", reaction='" + reaction + '\'' +
                ", date=" + date +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userAvatar=" + (userAvatar != null ? "[avatar data present]" : "null") +
                '}';
    }
}