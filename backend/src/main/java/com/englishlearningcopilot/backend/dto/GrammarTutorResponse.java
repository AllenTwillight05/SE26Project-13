package com.englishlearningcopilot.backend.dto;

public record GrammarTutorResponse(
        String reply,
        int relatedMistakeCount
) {
}
