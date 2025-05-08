package com.example.borrowit.Dto;

import com.example.borrowit.Entity.Feedback;
import com.example.borrowit.Entity.Reacts;
import com.example.borrowit.Entity.User;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

public class FeedbackDTO {
    private Long id;
    private String message;
    private LocalDateTime date;
    private List<String> reactTypes;
    private Long userId;
    private String userName;
    private String userEmail;
    private String reportReason;
    private boolean isReported;
    private String userAvatar;
    private Double sentimentScore;

    public FeedbackDTO(Feedback feedback) {
        // Basic feedback info
        this.id = feedback.getId();
        this.message = feedback.getMessage();
        this.date = feedback.getDate();
        this.reportReason = feedback.getReason();
        this.isReported = feedback.isReported();
        this.sentimentScore = feedback.getSentimentScore();


        // Debug information
        System.out.println("[DEBUG] Creating FeedbackDTO for feedback ID: " + feedback.getId());

        // Handle user data with null checks
        if (feedback.getUser() != null) {
            System.out.println("[DEBUG] Found user with ID: " + feedback.getUser().getId());

            this.userId = feedback.getUser().getId();
            this.userName = feedback.getUser().getName();
            this.userEmail = feedback.getUser().getEmail();

            // Process user image
            byte[] imageBytes = feedback.getUser().getImage();
            if (imageBytes != null && imageBytes.length > 0) {
                System.out.println("[DEBUG] User has image with size: " + imageBytes.length + " bytes");
                try {
                    this.userAvatar = Base64.getEncoder().encodeToString(imageBytes);
                    System.out.println("[DEBUG] Successfully encoded image to Base64");
                } catch (Exception e) {
                    System.err.println("[ERROR] Failed to encode user image: " + e.getMessage());
                    this.userAvatar = null;
                }
            } else {
                System.out.println("[DEBUG] User has no image or empty image data");
                this.userAvatar = null;
            }
        } else {
            System.out.println("[WARNING] Feedback has no associated user");
            this.userId = null;
            this.userName = null;
            this.userEmail = null;
            this.userAvatar = null;
        }

        // Handle reactions
        if (feedback.getReacts() != null && !feedback.getReacts().isEmpty()) {
            this.reactTypes = feedback.getReacts().stream()
                    .filter(react -> react.getReaction() != null)
                    .map(react -> react.getReaction().name())
                    .collect(Collectors.toList());
            System.out.println("[DEBUG] Found " + this.reactTypes.size() + " reactions");
        } else {
            this.reactTypes = new ArrayList<>();
            System.out.println("[DEBUG] No reactions found");
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public List<String> getReactTypes() {
        return reactTypes;
    }

    public void setReactTypes(List<String> reactTypes) {
        this.reactTypes = reactTypes;
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

    public String getReportReason() {
        return reportReason;
    }

    public void setReportReason(String reportReason) {
        this.reportReason = reportReason;
    }

    public boolean isReported() {
        return isReported;
    }

    public void setReported(boolean reported) {
        isReported = reported;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public Double getSentimentScore() {
        return sentimentScore;
    }

    public void setSentimentScore(Double sentimentScore) {
        this.sentimentScore = sentimentScore;
    }
}