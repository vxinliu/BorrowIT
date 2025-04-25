package com.example.borrowit.DTO;

import com.example.borrowit.Entity.Reacts;

public class ReactDTO {
    private String type;
    private Long feedbackId;

    public ReactDTO(Reacts react) {
        // Convert the enum to its string representation using .name()
        this.type = react.getReaction().name();
        this.feedbackId = react.getFeedback().getId();
    }

    // Getters and Setters
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Long getFeedbackId() { return feedbackId; }
    public void setFeedbackId(Long feedbackId) { this.feedbackId = feedbackId; }
}
