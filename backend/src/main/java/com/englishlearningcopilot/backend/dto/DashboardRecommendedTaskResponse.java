package com.englishlearningcopilot.backend.dto;

public record DashboardRecommendedTaskResponse(
        String scenarioId,
        String topic,
        String suggestedDuration,
        String intensity,
        String reason,
        Double score,
        String route
) {
}
