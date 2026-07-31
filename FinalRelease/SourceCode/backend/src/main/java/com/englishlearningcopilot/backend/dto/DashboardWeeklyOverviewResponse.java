package com.englishlearningcopilot.backend.dto;

public record DashboardWeeklyOverviewResponse(
        String speakingDuration,
        String pronunciationReference,
        String learningDays,
        String vocabularyLearned,
        String grammarPracticed
) {
}
