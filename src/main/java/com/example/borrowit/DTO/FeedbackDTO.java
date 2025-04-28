package com.example.borrowit.DTO;

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
    private String userEmail; // Add other user fields as needed

    private String reportReason;  // Add this field
    private boolean isReported;   // Add this field
    private String userAvatar;

    public void setSentimentScore(Double sentimentScore) {
        this.sentimentScore = sentimentScore;
    }

    public Double getSentimentScore() {
        return sentimentScore;
    }

    private Double sentimentScore; // Add the sentiment score field

    public void setReportReason(String reportReason) {
        this.reportReason = reportReason;
    }

    public void setReported(boolean reported) {
        isReported = reported;
    }

    public String getReportReason() {
        return reportReason;
    }

    public boolean isReported() {
        return isReported;
    }

    public FeedbackDTO(Feedback feedback) {
        this.id = feedback.getId();
        this.message = feedback.getMessage();
        this.date = feedback.getDate();
        // Add these lines to map the report information
        this.reportReason = feedback.getReason();  // Make sure Feedback has getReason()
        this.isReported = feedback.isReported();   // Make sure Feedback has isReported()
        this.sentimentScore = feedback.getSentimentScore(); // Ajoutez cette ligne
// In your Java FeedbackDTO class
        this.userAvatar = feedback.getUser().getImage() != null ?
                Base64.getEncoder().encodeToString(feedback.getUser().getImage()) :
                null;
        // Handle reacts
        this.reactTypes = feedback.getReacts() != null ?
                feedback.getReacts().stream()
                        .map(react -> react.getReaction().name())
                        .collect(Collectors.toList()) :
                new ArrayList<>();
        this.reactTypes = feedback.getReacts() != null ?
                feedback.getReacts().stream()
                        .map(react -> react.getReaction().name())
                        .collect(Collectors.toList()) :
                new ArrayList<>();

        // Handle user
        if (feedback.getUser() != null) {
            this.userId = feedback.getUser().getId();
            this.userName = feedback.getUser().getName();
            this.userEmail = feedback.getUser().getEmail();
            // Add other user fields as needed
        }
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public List<String> getReactTypes() { return reactTypes; }
    public void setReactTypes(List<String> reactTypes) { this.reactTypes = reactTypes; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
}