package com.englishlearningcopilot.backend.fsrs;

import java.time.Instant;
import java.util.Random;

/**
 * FSRS-6 interval review scheduler.
 */
public class FSRS {

    private final double requestRetention;
    private final double[] w;
    private final double decay;
    private final double factor;
    private final boolean enableFuzz;

    public FSRS(double requestRetention, double[] params) {
        if (params.length < 21) {
            throw new IllegalArgumentException("FSRS-6 requires 21 parameters");
        }
        this.requestRetention = requestRetention;
        this.w = params;
        this.decay = -params[20];
        this.factor = Math.pow(0.9, 1.0 / decay) - 1;
        this.enableFuzz = true;
    }

    public static double[] defaultParams() {
        return new double[] {
                0.212, 1.2931, 2.3065, 8.2956,
                6.4133, 0.8334,
                3.0194, 0.001,
                1.8722, 0.1666, 0.796,
                1.4835, 0.0614, 0.2629, 1.6483,
                0.6014, 0.8729,
                0.5425, 0.0912, 0.0658,
                0.1542
        };
    }

    public CardState review(CardState card, Rating rating) {
        Instant now = Instant.now();
        double nextD;
        double nextS;

        if (card.state == CardState.State.New) {
            nextD = initDifficulty(rating);
            nextS = initStability(rating);
            card.reps = 1;
        } else if (isSameDay(card, now)) {
            nextD = nextDifficulty(card.difficulty, rating);
            nextS = nextShortTermStability(card.stability, rating);
        } else if (rating == Rating.Again) {
            double elapsed = Math.max(1, card.getElapsedDays(now));
            double r = forgettingCurve(elapsed, card.stability);
            nextD = nextDifficulty(card.difficulty, rating);
            nextS = nextForgetStability(card.difficulty, card.stability, r);
            card.lapses++;
        } else {
            double elapsed = Math.max(1, card.getElapsedDays(now));
            double r = forgettingCurve(elapsed, card.stability);
            nextD = nextDifficulty(card.difficulty, rating);
            nextS = nextRecallStability(card.difficulty, card.stability, r, rating);
            card.reps++;
        }

        int interval = nextInterval(nextS, card.interval);
        card.difficulty = clamp(nextD, 1.0, 10.0);
        card.stability = nextS;
        card.interval = interval;
        card.due = now.plusSeconds((long) interval * 86400);
        card.lastReview = now;
        card.state = CardState.State.Review;
        return card;
    }

    private boolean isSameDay(CardState card, Instant now) {
        long ms = now.toEpochMilli() - card.lastReview.toEpochMilli();
        return card.state == CardState.State.Review && ms < 86400000;
    }

    private double initDifficulty(Rating rating) {
        double raw = w[4] - Math.exp(w[5] * (rating.value - 1)) + 1;
        return clamp(raw, 1.0, 10.0);
    }

    private double initStability(Rating rating) {
        return Math.max(w[rating.value - 1], 0.1);
    }

    private double forgettingCurve(double elapsedDays, double stability) {
        return Math.pow(1 + factor * elapsedDays / stability, decay);
    }

    private double nextDifficulty(double currentD, Rating rating) {
        double deltaD = -w[6] * (rating.value - 3);
        double damped = linearDamping(deltaD, currentD);
        double nextD = currentD + damped;
        double initEasy = initDifficulty(Rating.Easy);
        return meanReversion(initEasy, nextD);
    }

    private double linearDamping(double delta, double oldD) {
        return delta * (10 - oldD) / 9;
    }

    private double meanReversion(double initD, double nextD) {
        return w[7] * initD + (1 - w[7]) * nextD;
    }

    private double nextRecallStability(double difficulty, double stability, double retrievability, Rating rating) {
        double hardPenalty = (rating == Rating.Hard) ? w[15] : 1.0;
        double easyBonus = (rating == Rating.Easy) ? w[16] : 1.0;
        double stabilityFactor = Math.exp(w[8])
                * (11 - difficulty)
                * Math.pow(stability, -w[9])
                * (Math.exp((1 - retrievability) * w[10]) - 1)
                * hardPenalty
                * easyBonus;
        return stability * (1 + stabilityFactor);
    }

    private double nextShortTermStability(double stability, Rating rating) {
        double sinc = Math.exp(w[17] * (rating.value - 3 + w[18])) * Math.pow(stability, -w[19]);
        if (rating.value >= 3) {
            sinc = Math.max(sinc, 1.0);
        }
        return stability * sinc;
    }

    private double nextForgetStability(double difficulty, double stability, double retrievability) {
        double sMin = stability / Math.exp(w[17] * w[18]);
        double result = w[11]
                * Math.pow(difficulty, -w[12])
                * (Math.pow(stability + 1, w[13]) - 1)
                * Math.exp((1 - retrievability) * w[14]);
        return Math.min(result, sMin);
    }

    private int nextInterval(double stability, int lastInterval) {
        double rawInterval = stability / factor * (Math.pow(requestRetention, 1.0 / decay) - 1);
        double fuzzed = applyFuzz(rawInterval, lastInterval);
        return (int) clamp(Math.round(fuzzed), 1, 36500);
    }

    private double applyFuzz(double interval, int scheduledDays) {
        if (!enableFuzz || interval < 2.5) {
            return interval;
        }
        int ivl = (int) Math.round(interval);
        int minIvl = Math.max(2, (int) Math.round(ivl * 0.95 - 1));
        int maxIvl = (int) Math.round(ivl * 1.05 + 1);
        double fuzzFactor = new Random().nextDouble();
        return Math.floor(fuzzFactor * (maxIvl - minIvl + 1) + minIvl);
    }

    private static double clamp(double v, double lo, double hi) {
        return Math.max(lo, Math.min(hi, v));
    }

    public enum Rating {
        Again(1),
        Hard(2),
        Good(3),
        Easy(4);

        private final int value;

        Rating(int value) {
            this.value = value;
        }

        public static Rating fromInt(int value) {
            for (Rating rating : values()) {
                if (rating.value == value) {
                    return rating;
                }
            }
            return Good;
        }
    }

    public static class CardState {
        public enum State {
            New,
            Review
        }

        public double difficulty = 2.5;
        public double stability = 2.5;
        public int interval = 0;
        public int reps = 0;
        public int lapses = 0;
        public State state = State.New;
        public Instant due = Instant.now();
        public Instant lastReview = Instant.now();

        long getElapsedDays(Instant now) {
            long ms = now.toEpochMilli() - lastReview.toEpochMilli();
            return Math.max(0, ms / 86400000);
        }
    }
}
