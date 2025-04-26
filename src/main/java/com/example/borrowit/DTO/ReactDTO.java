package com.example.borrowit.DTO;

import com.example.borrowit.Entity.Reacts;
import com.example.borrowit.Entity.User;
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
    private String userAvatarUrl;  // We'll convert byte[] to Base64 or use a URL
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

        // Enhanced user data handling
        User user = react.getUser();
        if (user != null) {
            this.userId = user.getId();
            this.userName = user.getName();
            this.userEmail = user.getEmail();
            this.userPhone = user.getPhone();
            this.userCin = user.getCin();
            this.userGenre = user.getGenre();

            // Handle image/avatar - convert byte[] to Base64 or use a service URL
            this.userAvatarUrl = convertImageToUrl(user.getImage());
        }
    }

    // Helper method to handle image conversion
    private String convertImageToUrl(byte[] imageData) {
        if (imageData == null || imageData.length == 0) {
            return null; // or return a default avatar URL
        }

        // Option 1: Convert to Base64 (simple but increases payload size)
        // return "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(imageData);

        // Option 2: Use a service endpoint (recommended for production)
        return "/api/users/" + this.userId + "/avatar"; // You'll need to create this endpoint
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getReaction() { return reaction; }
    public void setReaction(String reaction) { this.reaction = reaction; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public String getUserPhone() { return userPhone; }
    public void setUserPhone(String userPhone) { this.userPhone = userPhone; }

    public String getUserAvatarUrl() { return userAvatarUrl; }
    public void setUserAvatarUrl(String userAvatarUrl) { this.userAvatarUrl = userAvatarUrl; }

    public String getUserGenre() { return userGenre; }
    public void setUserGenre(String userGenre) { this.userGenre = userGenre; }

    public Long getUserCin() { return userCin; }
    public void setUserCin(Long userCin) { this.userCin = userCin; }

    public Long getFeedbackId() { return feedbackId; }
    public void setFeedbackId(Long feedbackId) { this.feedbackId = feedbackId; }

    @Override
    public String toString() {
        return "ReactDTO{" +
                "id=" + id +
                ", reaction='" + reaction + '\'' +
                ", date=" + date +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                '}';
    }
}