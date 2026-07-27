package com.englishlearningcopilot.backend.repository;

import com.englishlearningcopilot.backend.entity.UserLearningPlan;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserLearningPlanRepository extends JpaRepository<UserLearningPlan, Long> {

    Optional<UserLearningPlan> findByUserId(Long userId);

    long countByUserId(Long userId);

    /**
     * Creates the default plan exactly once.  The unique key on {@code user_id}
     * makes the duplicate-key branch a no-op when concurrent requests arrive.
     */
    @Modifying
    @Query(value = """
            INSERT INTO user_learning_plan (
                user_id, daily_vocabulary_goal, daily_grammar_goal, enabled, created_at, updated_at
            ) VALUES (
                :userId, :dailyVocabularyGoal, :dailyGrammarGoal, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
            ) ON DUPLICATE KEY UPDATE user_id = user_id
            """, nativeQuery = true)
    void insertDefaultIfAbsent(
            @Param("userId") Long userId,
            @Param("dailyVocabularyGoal") int dailyVocabularyGoal,
            @Param("dailyGrammarGoal") int dailyGrammarGoal
    );
}
