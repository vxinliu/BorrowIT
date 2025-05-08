package com.example.borrowit.Dto;

import lombok.Data;


public class SentimentAnalysis {

    private String label; // "POSITIVE" ou "NEGATIVE"
    private Double score; // Score de confiance entre 0 et 1
    // Constructeur
    public SentimentAnalysis() {}

    public SentimentAnalysis(String label, Double score) {
        this.label = label;
        this.score = score;
    }

    // Getters et setters
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
}

