package com.example.borrowit.Controller;

// ChatController.java
import com.example.borrowit.Service.ChatGPTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "http://localhost:4200")
public class ChatController {

    @Autowired
    private ChatGPTService chatGPTService;

    @PostMapping
    public ResponseEntity<Map<String, String>> chat(@RequestBody Map<String, String> payload) {
        try {
            String message = payload.get("message");
            String response = chatGPTService.chatWithBot(message);
            return ResponseEntity.ok(Map.of("message", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erreur lors de la réponse du chatbot: " + e.getMessage()));
        }
    }
}

