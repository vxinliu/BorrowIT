package com.example.borrowit.DTO;

import com.example.borrowit.Entity.Feedback;
import com.example.borrowit.Entity.Reacts;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FeedbackDTO {
    private Long id;
    private String message;
    private LocalDateTime date;
    private List<String> reactTypes;  // List of react types or just some simplified data from the Reacts

    // Constructor to initialize DTO from Feedback entity
    public FeedbackDTO(Feedback feedback) {
        this.id = feedback.getId();
        this.message = feedback.getMessage();
        this.date = feedback.getDate();

        // Null check for reacts
        if (feedback.getReacts() != null) {
            this.reactTypes = feedback.getReacts().stream()
                    .map(react -> react.getReaction().name())  // Convert enum to string representation
                    .collect(Collectors.toList());
        } else {
            this.reactTypes = new ArrayList<>();
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
}
