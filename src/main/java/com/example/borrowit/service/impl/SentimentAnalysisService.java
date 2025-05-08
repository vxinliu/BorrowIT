package com.example.borrowit.service.impl;


import com.example.borrowit.Dto.FrenchSentimentAnalyzer;
import com.example.borrowit.Dto.SentimentAnalysis;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.Map;



@Service
public class SentimentAnalysisService {
    private final WebClient webClient;
    private final String apiKey;
    private static final Logger log = LoggerFactory.getLogger(SentimentAnalysisService.class);

    public SentimentAnalysisService(
            @Value("${huggingface.api.key}") String apiKey,
            @Value("${huggingface.model}") String modelName) {

        this.apiKey = apiKey;
        this.webClient = WebClient.builder()
                .baseUrl("https://api-inference.huggingface.co/models")
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .build();
    }

    public Mono<SentimentAnalysis> analyze(String text) {
        return analyzeWithHuggingFace(text)
                .onErrorResume(e -> {
                    log.error("HuggingFace API failed, using French fallback", e);
                    return useFrenchFallback(text);
                })
                .defaultIfEmpty(new SentimentAnalysis("NEUTRAL", 0.5));
    }

    private Mono<SentimentAnalysis> analyzeWithHuggingFace(String text) {
        if (apiKey == null || apiKey.isBlank()) {
            log.warn("No HuggingFace API key configured, skipping API call");
            return Mono.empty();
        }

        return webClient.post()
                .uri("/cardiffnlp/twitter-xlm-roberta-base-sentiment")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of("inputs", text))
                .retrieve()
                .bodyToMono(List.class)
                .timeout(Duration.ofSeconds(10))
                .doOnNext(response -> log.debug("Raw API response: {}", response))  // Add this line
                .map(this::parseResponse)
                .doOnError(e -> log.error("API call failed", e));
    }
    private SentimentAnalysis parseResponse(List<Object> response) {
        try {
            // Get the first list element which contains all sentiment results
            List<Map<String, Object>> sentimentResults = (List<Map<String, Object>>) response.get(0);

            // Find the result with highest score
            Map<String, Object> topResult = sentimentResults.stream()
                    .max(Comparator.comparingDouble(r -> ((Number) r.get("score")).doubleValue()))
                    .orElseThrow(() -> new RuntimeException("No sentiment results found"));

            String label = ((String) topResult.get("label")).toUpperCase();
            double score = ((Number) topResult.get("score")).doubleValue();

            // Map the labels to your expected format
            return switch (label) {
                case "POSITIVE" -> new SentimentAnalysis("POSITIVE", score);
                case "NEUTRAL" -> new SentimentAnalysis("NEUTRAL", score);
                case "NEGATIVE" -> new SentimentAnalysis("NEGATIVE", score);
                default -> throw new RuntimeException("Unknown label: " + label);
            };
        } catch (Exception e) {
            log.error("Failed to parse API response: {}", response, e);
            throw new RuntimeException("Invalid API response format", e);
        }
    }

    private Mono<SentimentAnalysis> useFrenchFallback(String text) {
        return Mono.fromCallable(() -> {
            log.info("Using French fallback analyzer");
            SentimentAnalysis result = FrenchSentimentAnalyzer.analyze(text);
            log.info("Fallback result - Label: {}, Score: {}", result.getLabel(), result.getScore());
            return result;
        }).onErrorResume(e -> {
            log.error("French fallback failed", e);
            return Mono.just(new SentimentAnalysis("NEUTRAL", 0.5));
        });
    }
}