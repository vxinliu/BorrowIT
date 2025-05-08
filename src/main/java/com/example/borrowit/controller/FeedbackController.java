package com.example.borrowit.controller;


import com.example.borrowit.Dto.FeedbackDTO;
import com.example.borrowit.Dto.FeedbackRequestDTO;
import com.example.borrowit.Dto.SentimentAnalysis;
import com.example.borrowit.Entity.Feedback;
import com.example.borrowit.Entity.User;
import com.example.borrowit.repository.UserRepository;
import com.example.borrowit.service.IFeedbackService;
import com.example.borrowit.service.IReactsService;
import com.example.borrowit.service.impl.BadWordFilterService;
import com.example.borrowit.service.impl.SentimentAnalysisService;
import com.example.borrowit.service.impl.UserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/feedbacks")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

public class FeedbackController {

    @Autowired
    public IFeedbackService feedbackService;
    public IReactsService reactsService;
    private final BadWordFilterService badWordFilterService;
    private final SentimentAnalysisService sentimentAnalysisService;
    private  final UserService userService;
    private static final Logger log = LoggerFactory.getLogger(SentimentAnalysisService.class);
    public FeedbackController(IFeedbackService feedbackService, IReactsService reactsService, BadWordFilterService badWordFilterService, SentimentAnalysisService sentimentAnalysisService, UserService userService) {
        this.feedbackService = feedbackService;
        this.reactsService = reactsService;
        this.badWordFilterService = badWordFilterService;
        this.sentimentAnalysisService=sentimentAnalysisService;
        this.userService=userService;
    }

    // Retrieve all feedbacks
    @GetMapping("/retrieve-all-feedbacks")
    public List<FeedbackDTO> getFeedbacks() {
        return feedbackService.retrieveAllFeedbacks()
                .stream()
                .map(FeedbackDTO::new)
                .toList();
    }


    // Retrieve a single feedback by ID
    @GetMapping("/retrieve-feedback/{id}")
    public Feedback getFeedbackById(@PathVariable("id") Long feedbackId) {
        return feedbackService.retrieveFeedback(feedbackId);
    }

    // Add a new feedback with associated reacts
   /* @PostMapping("/add-feedback")
    public Feedback addFeedback(@RequestBody Feedback f) {
        return feedbackService.addFeedback(f);
    }*/

    // Update an existing feedback
    @PutMapping("/update-feedback")
    public ResponseEntity<FeedbackDTO> updateFeedback(@RequestBody FeedbackRequestDTO requestDTO) {
        try {
            Feedback updatedFeedback = feedbackService.modifyFeedback(requestDTO);
            return ResponseEntity.ok(new FeedbackDTO(updatedFeedback));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
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
    public List<FeedbackDTO> getReportedFeedbacks() {
        return feedbackService.retrieveReportedFeedbacks()
                .stream()
                .map(FeedbackDTO::new)
                .collect(Collectors.toList());
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


    // Modifiez le endpoint addFeedback
    @PostMapping("/add-feedback")
    public ResponseEntity<?> addFeedback(
            @RequestBody Feedback feedback,
            @RequestHeader("Authorization") String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Unauthorized access attempt");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (badWordFilterService.containsBadWords(feedback.getMessage())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Inappropriate language detected"));
        }

        try {
            log.info("Analyzing sentiment for message: {}", feedback.getMessage());

            SentimentAnalysis analysis = sentimentAnalysisService.analyze(feedback.getMessage())
                    .block(Duration.ofSeconds(15));

            // Assurez-vous que l'analyse n'est pas null
            if (analysis == null) {
                analysis = new SentimentAnalysis("NEUTRAL", 0.5);
            }

            log.info("Final analysis - Label: {}, Score: {}", analysis.getLabel(), analysis.getScore());

            feedback.setSentimentScore(analysis.getScore());
            feedback.setSuggestedReaction(determineReaction(analysis));
            feedback.setDate(LocalDateTime.now());

            Feedback savedFeedback = feedbackService.addFeedback(feedback);
            return ResponseEntity.ok(savedFeedback);

        } catch (Exception e) {
            log.error("Error processing feedback, using default values", e);
            feedback.setSentimentScore(0.5);
            feedback.setSuggestedReaction("NEUTRAL");
            feedback.setDate(LocalDateTime.now());

            Feedback savedFeedback = feedbackService.addFeedback(feedback);
            return ResponseEntity.ok(savedFeedback);
        }
    }

    private String determineReaction(SentimentAnalysis analysis) {
        if (analysis == null) {
            return "NEUTRAL";
        }

        if ("POSITIVE".equalsIgnoreCase(analysis.getLabel())) {
            return analysis.getScore() > 0.85 ? "LOVE" : "LIKE";
        } else if ("NEGATIVE".equalsIgnoreCase(analysis.getLabel())) {
            return analysis.getScore() > 0.85 ? "ANGRY" : "SAD";
        }
        return "NEUTRAL";
    }

    // In FeedbackController.java
    @GetMapping("/retrieve-user-feedbacks/{userId}")
    public List<FeedbackDTO> getFeedbacksByUser(@PathVariable("userId") Long userId) {
        return feedbackService.retrieveFeedbacksByUser(userId)
                .stream()
                .map(FeedbackDTO::new)
                .collect(Collectors.toList());
    }


    @GetMapping("/test-feedback/{id}")
    public ResponseEntity<String> testFeedbackImage(@PathVariable Long id) {
        Feedback feedback = feedbackService.retrieveFeedback(id);
        if (feedback == null || feedback.getUser() == null) {
            return ResponseEntity.notFound().build();
        }

        byte[] imageBytes = feedback.getUser().getImage();
        if (imageBytes == null) {
            return ResponseEntity.ok("Feedback found but user has no image");
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(new String(imageBytes));
    }





}