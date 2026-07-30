package com.englishlearningcopilot.backend.dto;

import java.util.List;

public record PetChatResponse(
        String reply,
        SpeakingRecommendation speaking,
        VocabularyRecommendation vocabulary,
        List<String> retrievedContexts
) {

    public record SpeakingRecommendation(
            String scenarioId,
            String title,
            String reason,
            String route
    ) {
    }

    public record VocabularyRecommendation(
            String level,
            String title,
            String reason,
            String route
    ) {
    }

}
