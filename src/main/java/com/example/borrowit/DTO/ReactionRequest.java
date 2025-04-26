package com.example.borrowit.DTO;

import com.example.borrowit.Entity.Reacts;

public class ReactionRequest {
    private Long feedbackId;
    private Long userId;
    private Reacts.Reaction reactionType;

    // getters and setters

    public Long getFeedbackId() {
        return feedbackId;
    }

    public Long getUserId() {
        return userId;
    }

    public Reacts.Reaction getReactionType() {
        return reactionType;
    }

    public void setFeedbackId(Long feedbackId) {
        this.feedbackId = feedbackId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setReactionType(Reacts.Reaction reactionType) {
        this.reactionType = reactionType;
    }
}
