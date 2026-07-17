package com.englishlearningcopilot.backend.dto;

import java.util.List;

public record SpeakingFeedbackResponse(
        int totalScore,
        int pronunciation,
        int fluency,
        String speed,
        List<String> issueSentences,
        List<String> suggestions
) {
}
