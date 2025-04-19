package com.example.borrowit.controller;

import com.example.borrowit.Entity.Feedback;
import com.example.borrowit.service.IFeedbackService;
import com.example.borrowit.service.IReactsService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("/feedbacks")
public class FeedbackController {

    @Autowired
    public IFeedbackService feedbackService;
    public IReactsService reactsService;

    // Retrieve all feedbacks
    @GetMapping("/retrieve-all-feedbacks")
    public List<Feedback> getFeedbacks() {
        return feedbackService.retrieveAllFeedbacks();
    }

    // Retrieve a single feedback by ID
    @GetMapping("/retrieve-feedback/{id}")
    public Feedback getFeedbackById(@PathVariable("id") Long feedbackId) {
        return feedbackService.retrieveFeedback(feedbackId);
    }

    // Add a new feedback with associated reacts
    @PostMapping("/add-feedback")
    public Feedback addFeedback(@RequestBody Feedback f) {
        return feedbackService.addFeedback(f);
    }

    // Update an existing feedback
    @PutMapping("/update-feedback")
    public Feedback updateFeedback(@RequestBody Feedback f) {
        return feedbackService.modifyFeedback(f);
    }

    // Delete a feedback by ID
    @DeleteMapping("/delete-feedback/{id}")
    public void deleteFeedback(@PathVariable("id") Long feedbackId) {
        feedbackService.removeFeedback(feedbackId);
    }

    @GetMapping("/most-reacted")
    public ResponseEntity<List<Feedback>> getMostReactedFeedbacks(@RequestParam int topN) {
        List<Feedback> mostReactedFeedbacks = feedbackService.getMostReactedFeedbacks(topN);
        return ResponseEntity.ok(mostReactedFeedbacks);
    }

    @PutMapping(value = "/report-feedback/{id}", produces = "text/plain")
    public ResponseEntity<String> reportFeedback(@PathVariable("id") Long feedbackId, @RequestParam String reason) {
        try {
            feedbackService.reportFeedback(feedbackId, reason);
            return ResponseEntity.ok("Feedback reported successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("Feedback not found");
        }
    }



    @GetMapping("/retrieve-reported-feedbacks")
    public List<Feedback> getReportedFeedbacks() {
        return feedbackService.retrieveReportedFeedbacks();
    }

    @DeleteMapping("/delete-reported-feedback/{id}")
    public void deleteReportedFeedback(@PathVariable("id") Long feedbackId) {
        feedbackService.removeFeedback(feedbackId);
    }

    // Reject a reported feedback by ID
    @PutMapping("/reject-feedback/{id}")
    public ResponseEntity<String> rejectFeedback(@PathVariable("id") Long feedbackId) {
        try {
            Feedback updatedFeedback = feedbackService.rejectFeedback(feedbackId);
            return ResponseEntity.ok("Feedback rejected successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("Feedback not found");
        }
    }







}