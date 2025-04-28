package com.example.borrowit.Service;
// ChatGPTService.java
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class ChatGPTService {

    @Value("${openrouter.api.key}")
    private String apiKey;
    private static final String apiURL = "https://openrouter.ai/api/v1/chat/completions";

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    public String chatWithBot(String message) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("User-Agent", "MrammaAI (lindamlika865@gmail.com)");

        String requestBody = """
        {
         "model": "mistralai/mistral-7b-instruct",
          "messages": [
            {"role": "system", "content": "You are a helpful assistant that answers the user messages."},
            {"role": "user", "content": "%s"}
          ]
        }
        """.formatted(message);

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(apiURL, entity, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                JsonNode json = mapper.readTree(response.getBody());
                return json.at("/choices/0/message/content").asText();
            } else {
                throw new RuntimeException("Erreur API: " + response.getStatusCode() + " - " + response.getBody());
            }
        } catch (Exception e) {
            throw new RuntimeException("Erreur dans le chatbot: " + e.getMessage());
        }
    }
}

