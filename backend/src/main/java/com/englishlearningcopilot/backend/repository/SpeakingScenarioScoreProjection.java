package com.englishlearningcopilot.backend.repository;

public interface SpeakingScenarioScoreProjection {

    String getScenarioId();

    String getTitle();

    String getDuration();

    String getDifficulty();

    Double getAverageScore();
}
