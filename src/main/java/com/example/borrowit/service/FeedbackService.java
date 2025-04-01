package com.example.borrowit.service;

import com.example.borrowit.Entity.Feedback;

import java.util.List;

public interface FeedbackService {
    List<Feedback> retrieveAllFeedbacks();
    Feedback retrieveFeedback(Long id);
    Feedback addFeedback(Feedback feedback);
    void removeFeedback(Long id);
    Feedback modifyFeedback(Feedback feedback);
}
