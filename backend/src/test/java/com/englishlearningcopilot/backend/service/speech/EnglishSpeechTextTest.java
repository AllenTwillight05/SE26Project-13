package com.englishlearningcopilot.backend.service.speech;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class EnglishSpeechTextTest {

    @Test
    void acceptsEnglishTranscriptForPronunciationEvaluation() {
        assertThat(EnglishSpeechText.isEligibleForPronunciationEvaluation(
                "I would like to book a room, please."
        )).isTrue();
    }

    @Test
    void detectsChineseCharactersInChineseAndMixedTranscripts() {
        assertThat(EnglishSpeechText.containsChineseCharacters("这个怎么说？")).isTrue();
        assertThat(EnglishSpeechText.containsChineseCharacters("这个 restaurant 怎么读？")).isTrue();
        assertThat(EnglishSpeechText.containsChineseCharacters("How are you today?")).isFalse();
    }

    @Test
    void rejectsChineseAndMixedLanguageHelpRequests() {
        assertThat(EnglishSpeechText.isEligibleForPronunciationEvaluation("这个怎么说？")).isFalse();
        assertThat(EnglishSpeechText.isEligibleForPronunciationEvaluation("这个 restaurant 怎么读？")).isFalse();
    }

    @Test
    void rejectsBlankTranscript() {
        assertThat(EnglishSpeechText.isEligibleForPronunciationEvaluation("   ")).isFalse();
    }
}
