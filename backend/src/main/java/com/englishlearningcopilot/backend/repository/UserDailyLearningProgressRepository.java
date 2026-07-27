package com.englishlearningcopilot.backend.repository;

import com.englishlearningcopilot.backend.entity.UserDailyLearningProgress;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserDailyLearningProgressRepository extends JpaRepository<UserDailyLearningProgress, Long> {

    Optional<UserDailyLearningProgress> findByUserIdAndPlanDate(Long userId, LocalDate planDate);

    long countByUserIdAndPlanDate(Long userId, LocalDate planDate);

    /**
     * Creates one progress row for a user and date. Concurrent attempts are
     * resolved by the table's {@code (user_id, plan_date)} unique key.
     */
    @Modifying
    @Query(value = """
            INSERT INTO user_daily_learning_progress (
                user_id, plan_date, vocabulary_completed, grammar_completed,
                vocabulary_goal, grammar_goal, completed, created_at, updated_at
            ) VALUES (
                :userId, :planDate, 0, 0,
                :vocabularyGoal, :grammarGoal, FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
            ) ON DUPLICATE KEY UPDATE user_id = user_id
            """, nativeQuery = true)
    void insertIfAbsent(
            @Param("userId") Long userId,
            @Param("planDate") LocalDate planDate,
            @Param("vocabularyGoal") int vocabularyGoal,
            @Param("grammarGoal") int grammarGoal
    );

    @Modifying
    @Query(value = """
            UPDATE user_daily_learning_progress
            SET completed_at = CASE
                    WHEN vocabulary_completed + 1 >= vocabulary_goal
                         AND grammar_completed >= grammar_goal
                    THEN CASE WHEN completed THEN completed_at ELSE CURRENT_TIMESTAMP END
                    ELSE NULL
                END,
                completed = CASE
                    WHEN vocabulary_completed + 1 >= vocabulary_goal
                         AND grammar_completed >= grammar_goal
                    THEN TRUE
                    ELSE FALSE
                END,
                vocabulary_completed = vocabulary_completed + 1,
                updated_at = CURRENT_TIMESTAMP
            WHERE user_id = :userId
              AND plan_date = :planDate
            """, nativeQuery = true)
    int incrementVocabularyCompletion(@Param("userId") Long userId, @Param("planDate") LocalDate planDate);

    @Modifying
    @Query(value = """
            UPDATE user_daily_learning_progress
            SET completed_at = CASE
                    WHEN vocabulary_completed >= vocabulary_goal
                         AND grammar_completed + 1 >= grammar_goal
                    THEN CASE WHEN completed THEN completed_at ELSE CURRENT_TIMESTAMP END
                    ELSE NULL
                END,
                completed = CASE
                    WHEN vocabulary_completed >= vocabulary_goal
                         AND grammar_completed + 1 >= grammar_goal
                    THEN TRUE
                    ELSE FALSE
                END,
                grammar_completed = grammar_completed + 1,
                updated_at = CURRENT_TIMESTAMP
            WHERE user_id = :userId
              AND plan_date = :planDate
            """, nativeQuery = true)
    int incrementGrammarCompletion(@Param("userId") Long userId, @Param("planDate") LocalDate planDate);

    List<UserDailyLearningProgress> findByUserIdAndCompletedTrueOrderByPlanDateDesc(Long userId);

    @Query("""
            SELECT COUNT(progress)
            FROM UserDailyLearningProgress progress
            WHERE progress.userId = :userId
              AND progress.planDate BETWEEN :startDate AND :endDate
              AND (progress.vocabularyCompleted > 0 OR progress.grammarCompleted > 0)
            """)
    long countLearningDaysInRange(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("""
            SELECT progress.planDate
            FROM UserDailyLearningProgress progress
            WHERE progress.userId = :userId
              AND progress.planDate BETWEEN :startDate AND :endDate
              AND (progress.vocabularyCompleted > 0 OR progress.grammarCompleted > 0)
            """)
    List<LocalDate> findLearningDatesInRange(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
