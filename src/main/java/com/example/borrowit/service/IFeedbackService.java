package com.example.borrowit.service;

import com.example.borrowit.Entity.Feedback;


import java.util.List;

public interface IFeedbackService {
    public List<Feedback> retrieveAllFeedbacks();
    public Feedback retrieveFeedback(Long feedbackId);
    public Feedback addFeedback(Feedback f);
    public void removeFeedback(Long feedbackId);
    public Feedback modifyFeedback(Feedback f);
}
