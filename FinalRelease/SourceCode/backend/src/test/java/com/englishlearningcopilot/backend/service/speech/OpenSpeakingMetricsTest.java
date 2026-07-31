package com.englishlearningcopilot.backend.service.speech;

import static org.assertj.core.api.Assertions.assertThat;

import com.englishlearningcopilot.backend.entity.SpeakingMessage;
import java.util.List;
import org.junit.jupiter.api.Test;

class OpenSpeakingMetricsTest {

    @Test
    void referenceScoreWeightsAccuracyAndFluencyOnly() {
        assertThat(OpenSpeakingMetrics.referenceScore(new PronunciationScore(0, 80, 60, 10, 0)))
                .isEqualTo(73.0);
    }

    @Test
    void wordsPerMinuteReturnsZeroForMissingOrInvalidInputs() {
        SpeakingMessage blank = new SpeakingMessage();
        blank.setContent(" ");
        blank.setDurationMs(1000L);
        SpeakingMessage noDuration = new SpeakingMessage();
        noDuration.setContent("hello");
        SpeakingMessage nonPositiveDuration = new SpeakingMessage();
        nonPositiveDuration.setContent("hello");
        nonPositiveDuration.setDurationMs(0L);
        SpeakingMessage nullContent = new SpeakingMessage();
        nullContent.setDurationMs(1000L);

        assertThat(OpenSpeakingMetrics.wordsPerMinute((SpeakingMessage) null)).isZero();
        assertThat(OpenSpeakingMetrics.wordsPerMinute((String) null, 1000L)).isZero();
        assertThat(OpenSpeakingMetrics.wordsPerMinute("hello", null)).isZero();
        assertThat(OpenSpeakingMetrics.wordsPerMinute(List.of(
                blank,
                noDuration,
                nonPositiveDuration,
                nullContent
        ))).isZero();
    }

    @Test
    void wordsPerMinuteCountsEnglishWordsAcrossMessages() {
        SpeakingMessage first = new SpeakingMessage();
        first.setContent("Hello, I'm ready.");
        first.setDurationMs(30_000L);
        SpeakingMessage second = new SpeakingMessage();
        second.setContent("Book a room");
        second.setDurationMs(30_000L);

        assertThat(OpenSpeakingMetrics.wordsPerMinute(List.of(first, second))).isEqualTo(6.0);
        assertThat(OpenSpeakingMetrics.wordsPerMinute("One two three", 60_000L)).isEqualTo(3.0);
    }
}
