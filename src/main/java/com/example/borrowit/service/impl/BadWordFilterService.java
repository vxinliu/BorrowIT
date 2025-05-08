package com.example.borrowit.service.impl;


import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class BadWordFilterService {
    private static final List<String> BAD_WORDS = Arrays.asList(
            "con", "connard", "putain", "merde", "salope", "enculé"
    );

    public boolean containsBadWords(String text) {
        if (text == null) return false;
        String lowerCaseText = text.toLowerCase();
        return BAD_WORDS.stream().anyMatch(badWord ->
                lowerCaseText.contains(badWord.toLowerCase())
        );
    }
}
