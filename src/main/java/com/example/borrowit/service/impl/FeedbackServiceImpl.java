package com.example.borrowit.service.impl;

import com.example.borrowit.Entity.Feedback;
import com.example.borrowit.Entity.Item;
import com.example.borrowit.repository.FeedbackRepository;
import com.example.borrowit.repository.ItemRepository;
import com.example.borrowit.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackServiceImpl implements FeedbackService {
    @Autowired
    private FeedbackRepository feedbackRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Override
    public List<Feedback> retrieveAllFeedbacks() {
        return feedbackRepository.findAll();
    }

    @Override
    public Feedback retrieveFeedback(Long id) {
        return feedbackRepository.findById(id).orElse(null);
    }

    @Override
    public Feedback addFeedback(Feedback feedback, Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        feedback.setItem(item); // Set the managed Item
        return feedbackRepository.save(feedback);
    }


    @Override
    public void removeFeedback(Long id) {
        feedbackRepository.deleteById(id);
    }

    @Override
    public Feedback modifyFeedback(Feedback feedback, Long itemId) {
        // Retrieve the existing feedback
        Feedback existingFeedback = feedbackRepository.findById(feedback.getId())
                .orElseThrow(() -> new RuntimeException("Feedback not found"));

        // Update the existing feedback with the new values
        existingFeedback.setMessage(feedback.getMessage());
        existingFeedback.setDate(feedback.getDate());

        // Update the item based on the provided itemId
        if (itemId != null) {
            Item item = itemRepository.findById(itemId)
                    .orElseThrow(() -> new RuntimeException("Item not found"));
            existingFeedback.setItem(item); // Set the new Item
        }

        // Save and return the updated feedback
        return feedbackRepository.save(existingFeedback);
    }
}