package com.englishlearningcopilot.backend.fsrs;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.englishlearningcopilot.backend.fsrs.FSRS.CardState;
import com.englishlearningcopilot.backend.fsrs.FSRS.Rating;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

class FSRSTest {

    private final FSRS fsrs = new FSRS(0.9, FSRS.defaultParams());

    @Test
    void constructorRejectsParameterSetsThatAreTooShort() {
        assertThatThrownBy(() -> new FSRS(0.9, new double[20]))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("FSRS-6 requires 21 parameters");
    }

    @ParameterizedTest
    @EnumSource(Rating.class)
    void reviewInitializesNewCardsForEveryRating(Rating rating) {
        CardState card = new CardState();

        CardState reviewed = fsrs.review(card, rating);

        assertThat(reviewed.state).isEqualTo(CardState.State.Review);
        assertThat(reviewed.reps).isEqualTo(1);
        assertThat(reviewed.lapses).isZero();
        assertThat(reviewed.interval).isGreaterThanOrEqualTo(1);
        assertThat(reviewed.difficulty).isBetween(1.0, 10.0);
        assertThat(reviewed.stability).isGreaterThanOrEqualTo(0.1);
        assertThat(reviewed.due).isAfter(reviewed.lastReview);
    }

    @ParameterizedTest
    @EnumSource(Rating.class)
    void reviewSameDayCardsUsesShortTermStabilityPath(Rating rating) {
        CardState card = reviewCard();
        card.lastReview = Instant.now().minusSeconds(60);
        card.reps = 4;
        card.lapses = 1;

        CardState reviewed = fsrs.review(card, rating);

        assertThat(reviewed.state).isEqualTo(CardState.State.Review);
        assertThat(reviewed.reps).isEqualTo(4);
        assertThat(reviewed.lapses).isEqualTo(1);
        assertThat(reviewed.interval).isGreaterThanOrEqualTo(1);
        assertThat(reviewed.difficulty).isBetween(1.0, 10.0);
    }

    @ParameterizedTest
    @EnumSource(value = Rating.class, names = {"Hard", "Good", "Easy"})
    void reviewElapsedRecallCardsUsesRecallStabilityPath(Rating rating) {
        CardState card = reviewCard();
        card.lastReview = Instant.now().minusSeconds(5 * 86_400L);
        card.reps = 3;

        CardState reviewed = fsrs.review(card, rating);

        assertThat(reviewed.reps).isEqualTo(4);
        assertThat(reviewed.lapses).isZero();
        assertThat(reviewed.interval).isGreaterThanOrEqualTo(1);
        assertThat(reviewed.stability).isPositive();
    }

    @Test
    void reviewElapsedAgainCardRecordsLapseAndForgetStability() {
        CardState card = reviewCard();
        card.lastReview = Instant.now().minusSeconds(7 * 86_400L);
        card.reps = 2;

        CardState reviewed = fsrs.review(card, Rating.Again);

        assertThat(reviewed.reps).isEqualTo(2);
        assertThat(reviewed.lapses).isEqualTo(1);
        assertThat(reviewed.interval).isGreaterThanOrEqualTo(1);
    }

    @Test
    void oldReviewCardDoesNotUseSameDayShortTermPath() {
        CardState card = reviewCard();
        card.lastReview = Instant.now().minusSeconds(2 * 86_400L);
        card.reps = 1;

        CardState reviewed = fsrs.review(card, Rating.Good);

        assertThat(reviewed.reps).isEqualTo(2);
    }

    @Test
    void reviewClampsDifficultyAndIntervalBounds() {
        double[] params = FSRS.defaultParams();
        params[0] = 0.01;
        params[1] = 0.01;
        params[2] = 0.01;
        params[3] = 100_000.0;
        params[4] = 100.0;
        FSRS highDifficultyFsrs = new FSRS(0.9, params);

        CardState easy = highDifficultyFsrs.review(new CardState(), Rating.Easy);
        CardState again = highDifficultyFsrs.review(new CardState(), Rating.Again);

        assertThat(easy.difficulty).isEqualTo(10.0);
        assertThat(easy.interval).isLessThanOrEqualTo(36_500);
        assertThat(again.stability).isGreaterThanOrEqualTo(0.1);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 0, 5, -1})
    void ratingFromIntMapsKnownValuesAndDefaultsToGood(int value) {
        Rating rating = Rating.fromInt(value);

        if (value == 1) {
            assertThat(rating).isEqualTo(Rating.Again);
        } else if (value == 2) {
            assertThat(rating).isEqualTo(Rating.Hard);
        } else if (value == 4) {
            assertThat(rating).isEqualTo(Rating.Easy);
        } else {
            assertThat(rating).isEqualTo(Rating.Good);
        }
    }

    @Test
    void elapsedDaysNeverGoesNegative() {
        CardState card = new CardState();
        Instant now = Instant.now();
        card.lastReview = now.plusSeconds(86_400);

        assertThat(card.getElapsedDays(now)).isZero();
    }

    private static CardState reviewCard() {
        CardState card = new CardState();
        card.state = CardState.State.Review;
        card.difficulty = 5.0;
        card.stability = 4.0;
        card.interval = 3;
        card.reps = 1;
        card.lapses = 0;
        card.due = Instant.now().minusSeconds(60);
        card.lastReview = Instant.now().minusSeconds(3 * 86_400L);
        return card;
    }
}
