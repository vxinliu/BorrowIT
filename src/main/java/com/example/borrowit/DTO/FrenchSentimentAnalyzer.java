package com.example.borrowit.DTO;

import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import static java.util.Map.entry;

public class FrenchSentimentAnalyzer {
    private static final Map<String, Double> LEXICON = Map.ofEntries(
            entry("excellent", 0.95),
            entry("superbe", 0.9),
            entry("exceptionnel", 0.96),
            entry("parfait", 0.97),
            entry("ravi", 0.85),
            entry("content", 0.75),
            entry("satisfait", 0.8),
            entry("déçu", -0.85),
            entry("mauvais", -0.9),
            entry("horrible", -0.95),
            entry("nul", -0.92),
            entry("insatisfait", -0.8),
            // Ajoutez plus de mots ici
            entry("aimer", 0.8),
            entry("adorer", 0.9),
            entry("détester", -0.9),
            entry("haïr", -0.95)
    );

    private static final Set<String> NEGATIONS = Set.of("pas", "non", "ne", "ni", "sans", "aucun");
    private static final Set<String> INTENSIFIERS = Set.of("très", "vraiment", "absolument", "extrêmement", "tellement");
    private static final Pattern CLEAN_PATTERN = Pattern.compile("[^a-zéèêëàâùûüîïôœç]");

    public static SentimentAnalysis analyze(String text) {
        if (text == null || text.isBlank()) {
            return new SentimentAnalysis("NEUTRAL", 0.5);
        }

        String cleanedText = CLEAN_PATTERN.matcher(text.toLowerCase()).replaceAll(" ");
        String[] words = cleanedText.split("\\s+");

        double totalScore = 0;
        int wordCount = 0;
        boolean nextNegated = false;
        double intensity = 1.0;

        for (String word : words) {
            if (word.isBlank()) continue;

            if (NEGATIONS.contains(word)) {
                nextNegated = true;
                continue;
            }

            if (INTENSIFIERS.contains(word)) {
                intensity = 1.5;
                continue;
            }

            if (LEXICON.containsKey(word)) {
                double wordScore = LEXICON.get(word);
                wordScore = nextNegated ? -wordScore * 0.8 : wordScore;
                wordScore *= intensity;

                totalScore += wordScore;
                wordCount++;

                // Reset modifiers
                nextNegated = false;
                intensity = 1.0;
            }
        }

        if (wordCount == 0) {
            return new SentimentAnalysis("NEUTRAL", 0.5);
        }

        double averageScore = totalScore / wordCount;
        return determineSentiment(averageScore);
    }

    private static SentimentAnalysis determineSentiment(double score) {
        if (score > 0.3) {
            double normalizedScore = (score + 1) / 2; // Normalize to 0-1 range
            return new SentimentAnalysis("POSITIVE", Math.min(0.99, normalizedScore));
        } else if (score < -0.3) {
            double normalizedScore = (-score + 1) / 2; // Normalize to 0-1 range
            return new SentimentAnalysis("NEGATIVE", Math.min(0.99, normalizedScore));
        }
        return new SentimentAnalysis("NEUTRAL", 0.5);
    }
}