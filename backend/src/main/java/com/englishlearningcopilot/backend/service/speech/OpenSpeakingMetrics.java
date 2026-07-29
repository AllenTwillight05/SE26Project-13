package com.englishlearningcopilot.backend.service.speech;

import com.englishlearningcopilot.backend.entity.SpeakingMessage;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Learner-facing metrics for open speaking. ISE integrity is intentionally excluded because
 * open speaking uses an ASR transcript rather than a predefined reading reference.
 */
public final class OpenSpeakingMetrics {

    private static final Pattern ENGLISH_WORD_PATTERN = Pattern.compile("[A-Za-z]+(?:'[A-Za-z]+)?");

    private OpenSpeakingMetrics() {
    }

    public static double referenceScore(PronunciationScore score) {
        return round1(score.accuracy() * 0.65 + score.fluency() * 0.35);
    }

    public static double wordsPerMinute(SpeakingMessage message) {
        if (message == null) {
            return 0;
        }
        return wordsPerMinute(message.getContent(), message.getDurationMs());
    }

    public static double wordsPerMinute(List<SpeakingMessage> messages) {
        long durationMs = 0;
        int wordCount = 0;
        for (SpeakingMessage message : messages) {
            if (message.getDurationMs() == null || message.getDurationMs() <= 0
                    || message.getContent() == null || message.getContent().isBlank()) {
                continue;
            }
            durationMs += message.getDurationMs();
            wordCount += countEnglishWords(message.getContent());
        }
        return wordsPerMinute(wordCount, durationMs);
    }

    public static double wordsPerMinute(String text, Long durationMs) {
        if (durationMs == null) {
            return 0;
        }
        return wordsPerMinute(countEnglishWords(text), durationMs);
    }

    private static double wordsPerMinute(int wordCount, long durationMs) {
        if (durationMs <= 0 || wordCount == 0) {
            return 0;
        }
        return wordCount * 60_000.0 / durationMs;
    }

    private static int countEnglishWords(String text) {
        if (text == null || text.isBlank()) {
            return 0;
        }
        Matcher matcher = ENGLISH_WORD_PATTERN.matcher(text);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }

    private static double round1(double value) {
        return Math.round(value * 10.0) / 10.0;
    }
}
