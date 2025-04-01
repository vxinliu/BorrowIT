package com.example.borrowit.service.impl;

import com.example.borrowit.Entity.Feedback;
import com.example.borrowit.repository.FeedbackRepository;
import com.example.borrowit.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackServiceImpl implements FeedbackService {
    @Autowired
    private FeedbackRepository feedbackRepository;
    @Override
    public List<Feedback> retrieveAllFeedbacks() {
        return feedbackRepository.findAll();
    }

    @Override
    public Feedback retrieveFeedback(Long id) {
        return feedbackRepository.findById(id).orElse(null);
    }

    @Override
    public Feedback addFeedback(Feedback feedback) {
        return feedbackRepository.save(feedback);
    }

    @Override
    public void removeFeedback(Long id) {
        feedbackRepository.deleteById(id);
    }

    @Override
    public Feedback modifyFeedback(Feedback feedback) {
        return feedbackRepository.save(feedback);
    }
}