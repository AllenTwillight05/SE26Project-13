package com.englishlearningcopilot.backend.repository;

import com.englishlearningcopilot.backend.entity.LearningProgressOutboxEvent;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LearningProgressOutboxEventRepository
        extends JpaRepository<LearningProgressOutboxEvent, Long> {

    /**
     * The unique practice key makes request retries coalesce into one durable
     * work item before any background processing begins.
     */
    @Modifying
    @Query(value = """
            INSERT INTO learning_progress_outbox_events (
                user_id, plan_date, practice_type, item_id, status, attempts,
                available_at, created_at, updated_at
            ) VALUES (
                :userId, :planDate, :practiceType, :itemId, 'PENDING', 0,
                CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
            ) ON DUPLICATE KEY UPDATE user_id = user_id
            """, nativeQuery = true)
    int insertIfAbsent(
            @Param("userId") Long userId,
            @Param("planDate") LocalDate planDate,
            @Param("practiceType") String practiceType,
            @Param("itemId") String itemId
    );

    @Query("""
            SELECT event.id
            FROM LearningProgressOutboxEvent event
            WHERE (event.status = 'PENDING' AND event.availableAt <= :now)
               OR (event.status = 'PROCESSING' AND event.lockedUntil < :now)
            ORDER BY event.id
            """)
    List<Long> findReadyEventIds(@Param("now") Instant now, Pageable pageable);

    /**
     * Atomically leases one event. A stale lease can be reclaimed after a
     * process restart, while a live worker cannot be duplicated by another
     * application instance.
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            UPDATE LearningProgressOutboxEvent event
            SET event.status = 'PROCESSING',
                event.attempts = event.attempts + 1,
                event.lockedUntil = :lockedUntil,
                event.updatedAt = :now
            WHERE event.id = :eventId
              AND (
                    (event.status = 'PENDING' AND event.availableAt <= :now)
                    OR (event.status = 'PROCESSING' AND event.lockedUntil < :now)
              )
            """)
    int claimIfReady(
            @Param("eventId") Long eventId,
            @Param("now") Instant now,
            @Param("lockedUntil") Instant lockedUntil
    );

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            UPDATE LearningProgressOutboxEvent event
            SET event.status = 'COMPLETED',
                event.processedAt = :processedAt,
                event.lockedUntil = NULL,
                event.lastError = NULL,
                event.updatedAt = :processedAt
            WHERE event.id = :eventId
              AND event.status = 'PROCESSING'
            """)
    int markCompleted(@Param("eventId") Long eventId, @Param("processedAt") Instant processedAt);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            UPDATE LearningProgressOutboxEvent event
            SET event.status = 'PENDING',
                event.availableAt = :availableAt,
                event.lockedUntil = NULL,
                event.lastError = :lastError,
                event.updatedAt = :now
            WHERE event.id = :eventId
              AND event.status = 'PROCESSING'
            """)
    int reschedule(
            @Param("eventId") Long eventId,
            @Param("availableAt") Instant availableAt,
            @Param("lastError") String lastError,
            @Param("now") Instant now
    );
}
