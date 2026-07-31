package com.englishlearningcopilot.backend.repository;

import com.englishlearningcopilot.backend.entity.UserDailyPracticeLog;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserDailyPracticeLogRepository extends JpaRepository<UserDailyPracticeLog, Long> {

    long countByUserIdAndPlanDateAndPracticeTypeAndItemId(
            Long userId,
            LocalDate planDate,
            String practiceType,
            String itemId
    );

    /**
     * Records a practice item at most once per user and day. The unique key
     * resolves concurrent attempts without turning the duplicate into an error.
     *
     * @return 1 when a new row was inserted; 0 when it already existed
     */
    @Modifying
    @Query(value = """
            INSERT INTO user_daily_practice_log (
                user_id, plan_date, practice_type, item_id, created_at
            ) VALUES (
                :userId, :planDate, :practiceType, :itemId, CURRENT_TIMESTAMP
            ) ON DUPLICATE KEY UPDATE user_id = user_id
            """, nativeQuery = true)
    int insertIfAbsent(
            @Param("userId") Long userId,
            @Param("planDate") LocalDate planDate,
            @Param("practiceType") String practiceType,
            @Param("itemId") String itemId
    );

    long countByUserIdAndPlanDateBetweenAndPracticeType(
            Long userId,
            LocalDate startDate,
            LocalDate endDate,
            String practiceType
    );
}
