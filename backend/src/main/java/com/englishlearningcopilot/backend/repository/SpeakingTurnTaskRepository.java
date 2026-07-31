package com.englishlearningcopilot.backend.repository;

import com.englishlearningcopilot.backend.entity.SpeakingTurnTask;
import com.englishlearningcopilot.backend.entity.SpeakingTurnTaskStatus;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SpeakingTurnTaskRepository extends JpaRepository<SpeakingTurnTask, Long> {

    Optional<SpeakingTurnTask> findBySessionIdAndAttemptId(Long sessionId, String attemptId);

    List<SpeakingTurnTask> findBySessionIdOrderByCreatedAtAscIdAsc(Long sessionId);

    Optional<SpeakingTurnTask> findFirstByStatusAndAudioUrlIsNotNullAndAvailableAtLessThanEqualOrderByCreatedAtAsc(
            SpeakingTurnTaskStatus status,
            Instant availableAt
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT task FROM SpeakingTurnTask task WHERE task.id = :taskId")
    Optional<SpeakingTurnTask> findByIdForUpdate(@Param("taskId") Long taskId);

    /**
     * Future network retries use the same session/attempt key. The unique key
     * and this atomic insert ensure they resolve to one durable turn task.
     */
    @Modifying
    @Query(value = """
            INSERT INTO speaking_turn_tasks (
                session_id, user_message_id, attempt_id, status, attempt_count,
                available_at, created_at, updated_at, version
            ) VALUES (
                :sessionId, :userMessageId, :attemptId, 'PENDING', 0,
                CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0
            ) ON DUPLICATE KEY UPDATE session_id = session_id
            """, nativeQuery = true)
    int insertIfAbsent(
            @Param("sessionId") Long sessionId,
            @Param("userMessageId") Long userMessageId,
            @Param("attemptId") String attemptId
    );
}
