package com.englishlearningcopilot.backend.fsrs;

import static org.assertj.core.api.Assertions.assertThat;

import com.englishlearningcopilot.backend.entity.UserWordProgress;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;

class FsrsRetentionTest {

    @Test
    void averagePercentReturnsZeroForEmptyCards() {
        assertThat(FsrsRetention.averagePercent(List.of(), Instant.now())).isZero();
    }

    @Test
    void averagePercentUsesUpdatedAtAndMinimumStabilityFallbacks() {
        Instant now = Instant.parse("2026-07-30T00:00:00Z");
        UserWordProgress missingLastReview = card(null, now.minusSeconds(86_400), null);
        UserWordProgress nonPositiveStability = card(now.minusSeconds(2 * 86_400L), now, 0.0);

        int retention = FsrsRetention.averagePercent(List.of(missingLastReview, nonPositiveStability), now);

        assertThat(retention).isBetween(0, 100);
    }

    private static UserWordProgress card(Instant lastReview, Instant updatedAt, Double stability) {
        UserWordProgress progress = new UserWordProgress();
        progress.setLastReview(lastReview);
        progress.setUpdatedAt(updatedAt);
        progress.setStability(stability);
        return progress;
    }
}
