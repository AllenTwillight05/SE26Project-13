// FSRS-6 纯 Java 实现
// 来源：https://github.com/open-spaced-repetition/FSRS-Kotlin
// 适配 Spring Boot，纯 JVM，零外部依赖

import java.time.Instant;
import java.util.Random;

/**
 * FSRS-6 间隔复习调度器。
 *
 * 使用：
 *   FSRS fsrs = new FSRS(0.9, FSRS.defaultParams());
 *   CardState newState = fsrs.review(oldState, Rating.Good);
 */
public class FSRS {

    private final double requestRetention;
    private final double[] w;
    private final double decay;
    private final double factor;
    private final boolean enableFuzz;

    public FSRS(double requestRetention, double[] params) {
        if (params.length < 21) throw new IllegalArgumentException("FSRS-6 需要 21 个参数");
        this.requestRetention = requestRetention;
        this.w = params;
        this.decay = -params[20];
        this.factor = Math.pow(0.9, 1.0 / decay) - 1;
        this.enableFuzz = true;
    }

    /** FSRS-6 默认参数 */
    public static double[] defaultParams() {
        return new double[] {
            0.212, 1.2931, 2.3065, 8.2956,    // w[0..3] 初始稳定性
            6.4133, 0.8334,                     // w[4..5] 初始难度
            3.0194, 0.001,                      // w[6..7] 难度更新
            1.8722, 0.1666, 0.796,              // w[8..10] 稳定性更新
            1.4835, 0.0614, 0.2629, 1.6483,     // w[11..14] 遗忘后稳定性
            0.6014, 0.8729,                     // w[15..16] Hard/Easy 系数
            0.5425, 0.0912, 0.0658,             // w[17..19] 同日复习
            0.1542                               // w[20] 遗忘曲线衰减
        };
    }

    // ============ 核心入口 ============

    /** 答题后调用。返回更新后的 CardState。 */
    public CardState review(CardState card, Rating rating) {
        Instant now = Instant.now();
        double nextD, nextS;

        if (card.state == CardState.State.New) {
            // 第一次见 → 初始化
            nextD = initDifficulty(rating);
            nextS = initStability(rating);
            card.reps = 1;
        } else if (isSameDay(card, now)) {
            // 同日反复复习 → 用短时公式
            double elapsed = Math.max(0, card.getElapsedDays(now));
            double R = elapsed > 0 ? forgettingCurve(elapsed, card.stability) : 1.0;
            nextD = nextDifficulty(card.difficulty, rating);
            nextS = nextShortTermStability(card.stability, rating);
        } else if (rating == Rating.Again) {
            // 答错 → 遗忘公式
            double elapsed = Math.max(1, card.getElapsedDays(now));
            double R = forgettingCurve(elapsed, card.stability);
            nextD = nextDifficulty(card.difficulty, rating);
            nextS = nextForgetStability(card.difficulty, card.stability, R);
            card.lapses++;
        } else {
            // 答对，跨天 → 正常复习
            double elapsed = Math.max(1, card.getElapsedDays(now));
            double R = forgettingCurve(elapsed, card.stability);
            nextD = nextDifficulty(card.difficulty, rating);
            nextS = nextRecallStability(card.difficulty, card.stability, R, rating);
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

    /** 判断是否同日复习：距上次复习不足 1 天 */
    private boolean isSameDay(CardState card, Instant now) {
        long ms = now.toEpochMilli() - card.lastReview.toEpochMilli();
        return card.state == CardState.State.Review && ms < 86400000;
    }

    // ============ 初始化 ============

    private double initDifficulty(Rating r) {
        double raw = w[4] - Math.exp(w[5] * (r.value - 1)) + 1;
        return clamp(raw, 1.0, 10.0);
    }

    private double initStability(Rating r) {
        return Math.max(w[r.value - 1], 0.1);
    }

    // ============ 遗忘曲线：R(t, S) ============

    private double forgettingCurve(double elapsedDays, double stability) {
        return Math.pow(1 + factor * elapsedDays / stability, decay);
    }

    // ============ 难度更新 ============

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

    // ============ 稳定性更新（答对，跨天） ============

    private double nextRecallStability(double d, double s, double r, Rating rating) {
        double hardPenalty = (rating == Rating.Hard) ? w[15] : 1.0;
        double easyBonus = (rating == Rating.Easy) ? w[16] : 1.0;
        double stabilityFactor = Math.exp(w[8])
                * (11 - d)
                * Math.pow(s, -w[9])
                * (Math.exp((1 - r) * w[10]) - 1)
                * hardPenalty
                * easyBonus;
        return s * (1 + stabilityFactor);
    }

    // ============ 稳定性更新（同日复习） ============

    private double nextShortTermStability(double s, Rating rating) {
        double sinc = Math.exp(w[17] * (rating.value - 3 + w[18])) * Math.pow(s, -w[19]);
        if (rating.value >= 3) {
            sinc = Math.max(sinc, 1.0);
        }
        return s * sinc;
    }

    // ============ 稳定性更新（遗忘） ============

    private double nextForgetStability(double d, double s, double r) {
        double sMin = s / Math.exp(w[17] * w[18]);
        double result = w[11]
                * Math.pow(d, -w[12])
                * (Math.pow(s + 1, w[13]) - 1)
                * Math.exp((1 - r) * w[14]);
        return Math.min(result, sMin);
    }

    // ============ 间隔计算 ============

    private int nextInterval(double stability, int lastInterval) {
        double rawInterval = stability / factor * (Math.pow(requestRetention, 1.0 / decay) - 1);
        double fuzzed = applyFuzz(rawInterval, lastInterval);
        return (int) clamp(Math.round(fuzzed), 1, 36500);
    }

    /** 给间隔 ±5% 的随机抖动，防止一批题总在同一天出现 */
    private double applyFuzz(double interval, int scheduledDays) {
        if (!enableFuzz || interval < 2.5) return interval;
        int ivl = (int) Math.round(interval);
        int minIvl = Math.max(2, (int) Math.round(ivl * 0.95 - 1));
        int maxIvl = (int) Math.round(ivl * 1.05 + 1);
        double fuzzFactor = new Random().nextDouble();
        return Math.floor(fuzzFactor * (maxIvl - minIvl + 1) + minIvl);
    }

    private static double clamp(double v, double lo, double hi) {
        return Math.max(lo, Math.min(hi, v));
    }
}


// ============ 评分枚举 ============

enum Rating {
    Again(1), Hard(2), Good(3), Easy(4);
    final int value;
    Rating(int v) { this.value = v; }
    public static Rating fromInt(int v) {
        for (Rating r : values()) if (r.value == v) return r;
        return Good;
    }
}


// ============ 记忆状态 ============

class CardState {
    enum State { New, Review }

    double difficulty = 2.5;     // D，[1, 10]
    double stability = 2.5;      // S，天数
    int interval = 0;            // 本次安排的复习间隔
    int reps = 0;                // 总复习次数
    int lapses = 0;              // 总遗忘次数
    State state = State.New;
    Instant due = Instant.now();
    Instant lastReview = Instant.now();

    /** 距上次复习过了几天（最少返回 0） */
    long getElapsedDays(Instant now) {
        long ms = now.toEpochMilli() - lastReview.toEpochMilli();
        return Math.max(0, ms / 86400000);
    }
}
