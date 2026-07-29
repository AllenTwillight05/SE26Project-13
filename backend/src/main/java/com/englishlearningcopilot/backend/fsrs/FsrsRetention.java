package com.englishlearningcopilot.backend.fsrs;

import com.englishlearningcopilot.backend.entity.UserWordProgress;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

/** Calculates the current recall probability from the FSRS card state. */
public final class FsrsRetention {

    private static final double DECAY = -FSRS.defaultParams()[20];
    private static final double FACTOR = Math.pow(0.9, 1.0 / DECAY) - 1;

    private FsrsRetention() {
    }

    public static int averagePercent(List<UserWordProgress> cards, Instant now) {
        if (cards.isEmpty()) {
            return 0;
        }

        double average = cards.stream()
                .mapToDouble(card -> retention(card, now))
                .average()
                .orElse(0);
        return (int) Math.round(average * 100);
    }

    private static double retention(UserWordProgress card, Instant now) {
        Instant lastReview = card.getLastReview() == null ? card.getUpdatedAt() : card.getLastReview();
        double elapsedDays = lastReview == null
                ? 0
                : Math.max(0, Duration.between(lastReview, now).toDays());
        double stability = card.getStability() == null || card.getStability() <= 0
                ? 0.1
                : card.getStability();
        return Math.pow(1 + FACTOR * elapsedDays / stability, DECAY);
    }
}
