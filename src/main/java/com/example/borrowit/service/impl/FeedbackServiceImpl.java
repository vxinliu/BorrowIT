package com.example.borrowit.service.impl;

import com.example.borrowit.Entity.Feedback;
import com.example.borrowit.repository.FeedbackRepository;
import com.example.borrowit.repository.ReactsRepository;
import com.example.borrowit.service.IFeedbackService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FeedbackServiceImpl implements IFeedbackService {
    @Autowired
    private FeedbackRepository feedbackRepository;
    @Autowired
    private ReactsRepository reactsRepository;
    @Override
    @Transactional
    public List<Feedback> retrieveAllFeedbacks() {

         return feedbackRepository.findAll();
    }

    @Override
    public Feedback retrieveFeedback(Long feedbackId) {

        return feedbackRepository.findById(feedbackId).orElse(null);
    }

    @Override
    public Feedback addFeedback(Feedback f) {
        Feedback feedback = feedbackRepository.save(f);

        // Ensure reacts are associated with the saved feedback
        if (f.getReacts() != null) {
            f.getReacts().forEach(reacts -> {
                reacts.setFeedback(feedback);
                reactsRepository.save(reacts);
            });
        }

        return feedback;
    }

    @Override
    public void removeFeedback(Long feedbackId) {
        Optional<Feedback> feedback = feedbackRepository.findById(feedbackId);
        if (feedback.isPresent()) {
            feedbackRepository.deleteById(feedbackId);
        } else {
            throw new RuntimeException("Feedback not found with ID: " + feedbackId);
        }
    }



    @Override
    public Feedback modifyFeedback(Feedback f) {
        if (feedbackRepository.existsById(f.getId())) {
            return feedbackRepository.save(f);
        } else {
            throw new RuntimeException("Feedback not found with ID: " + f.getId());
        }
    }
}