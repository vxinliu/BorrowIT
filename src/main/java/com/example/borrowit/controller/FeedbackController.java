package com.example.borrowit.controller;

import com.example.borrowit.Entity.Feedback;
import com.example.borrowit.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/feedbacks")
public class FeedbackController {

    @Autowired
    public FeedbackService feedbackService;

    @GetMapping
    public List<Feedback> getAllFeedbacks() {
        return feedbackService.retrieveAllFeedbacks();
    }

    @GetMapping("/{id}")
    public Feedback getFeedbackById(@PathVariable Long id) {
        return feedbackService.retrieveFeedback(id);
    }

    @PostMapping("/add-feedback/{itemId}")
    public Feedback createFeedback(@RequestBody Feedback feedback, @PathVariable Long itemId) {
        return feedbackService.addFeedback(feedback, itemId);
    }


    @PutMapping("/modify-feedback/{itemId}")
    public Feedback updateFeedback(@RequestBody Feedback feedback, @PathVariable Long itemId) {
        return feedbackService.modifyFeedback(feedback, itemId);
    }

    @DeleteMapping("/{id}")
    public void deleteFeedback(@PathVariable Long id) {
        feedbackService.removeFeedback(id);
    }
}
