package com.example.borrowit.controller;

import com.example.borrowit.DTO.ReactDTO;
import com.example.borrowit.DTO.ReactionRequest;
import com.example.borrowit.Entity.Feedback;
import com.example.borrowit.Entity.Reacts;
import com.example.borrowit.service.IReactsService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/reacts")
public class ReactsController {
    @Autowired
    public IReactsService reactsService;

    @GetMapping("/retrieve-all-reacts")
    public ResponseEntity<List<ReactDTO>> getAllReacts() {
        List<ReactDTO> dtoList = reactsService.retrieveAllReacts()
                .stream().map(ReactDTO::new).toList();
        return ResponseEntity.ok(dtoList);
    }
    @GetMapping("/retrieve-react/{reactsId}")
    public Reacts getReactById(@PathVariable Long reactsId) {
        return reactsService.retrieveReacts(reactsId);
    }

    @PostMapping("/add-react")
    public ResponseEntity<ReactDTO> addReact(@RequestBody Reacts react) {
        Reacts savedReact = reactsService.addReact(react);
        ReactDTO dto = new ReactDTO(savedReact);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/modify-react")
    public Reacts modifyReact(@RequestBody Reacts react) {
        return reactsService.modifyReact(react);
    }

    @DeleteMapping("/remove-react/{reactId}")
    public void removeReact(@PathVariable Long reactId) {
        reactsService.removeReact(reactId);
    }

    // Controller method where you call reactsService.countReactionsForFeedback
    @GetMapping("/reactionCount/{feedbackId}")
    public ResponseEntity<Long> getReactionCountForFeedback(@PathVariable Long feedbackId) {
        long reactionCount = reactsService.countReactionsForFeedback(feedbackId);
        return ResponseEntity.ok(reactionCount);
    }
    @GetMapping("/retrieve-reacts-for-feedback/{feedbackId}")
    public ResponseEntity<List<ReactDTO>> getReactsForFeedback(@PathVariable Long feedbackId) {
        try {
            List<Reacts> reacts = reactsService.getReactsForFeedback(feedbackId);

            // Return empty array instead of null
            if (reacts == null || reacts.isEmpty()) {
                return ResponseEntity.ok(Collections.emptyList());
            }

            List<ReactDTO> dtos = reacts.stream()
                    .map(ReactDTO::new)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return ResponseEntity.ok(Collections.emptyList()); // Return empty array on error
        }
    }

    @PostMapping("/react")
    public ResponseEntity<ReactDTO> addOrUpdateReaction(
            @RequestBody ReactionRequest request) {
        Reacts react = reactsService.addOrUpdateReaction(
                request.getFeedbackId(),
                request.getUserId(),
                request.getReactionType());
        return ResponseEntity.ok(new ReactDTO(react));
    }

    @DeleteMapping("/react")
    public ResponseEntity<Void> removeReaction(
            @RequestParam Long feedbackId,
            @RequestParam Long userId) {
        reactsService.removeReaction(feedbackId, userId);
        return ResponseEntity.noContent().build();
    }

}
