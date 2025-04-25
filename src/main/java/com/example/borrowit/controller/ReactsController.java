package com.example.borrowit.controller;

import com.example.borrowit.DTO.ReactDTO;
import com.example.borrowit.Entity.Feedback;
import com.example.borrowit.Entity.Reacts;
import com.example.borrowit.service.IReactsService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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


}
