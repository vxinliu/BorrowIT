package com.example.borrowit.service;

import com.example.borrowit.DTO.FeedbackRequestDTO;
import com.example.borrowit.Entity.Feedback;


import java.util.List;

public interface IFeedbackService {
    public List<Feedback> retrieveAllFeedbacks();
    public Feedback retrieveFeedback(Long feedbackId);
    public Feedback addFeedback(Feedback f);
    public void removeFeedback(Long feedbackId);
    public List<Feedback> getMostReactedFeedbacks(int topN) ;
    public void reportFeedback(Long feedbackId, String reason);
    public List<Feedback> retrieveReportedFeedbacks();
    public Feedback rejectFeedback(Long feedbackId);
    public Feedback modifyFeedback(FeedbackRequestDTO requestDTO);
    List<Feedback> retrieveFeedbacksByUser(Long userId);
}
