package com.englishlearningcopilot.backend.repository;

import com.englishlearningcopilot.backend.entity.SpeakingSession;
import jakarta.persistence.LockModeType;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.query.Param;

public interface SpeakingSessionRepository extends JpaRepository<SpeakingSession, Long> {

    List<SpeakingSession> findByUserUsernameOrderByStartedAtDesc(String username);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT speakingSession FROM SpeakingSession speakingSession WHERE speakingSession.id = :sessionId")
    java.util.Optional<SpeakingSession> findByIdForUpdate(@Param("sessionId") Long sessionId);

    @Query("""
            SELECT session.scenario.id AS scenarioId,
                   session.scenario.title AS title,
                   session.scenario.duration AS duration,
                   session.scenario.difficulty AS difficulty,
                   AVG(message.pronunciationScore) AS averageScore
            FROM SpeakingMessage message
            JOIN message.session session
            WHERE session.user.id = :userId
              AND session.scenario.active = true
              AND session.scenario.id LIKE 'G-%'
              AND message.sender = com.englishlearningcopilot.backend.entity.SpeakingMessageSender.USER
              AND message.pronunciationScore IS NOT NULL
            GROUP BY session.scenario.id,
                     session.scenario.title,
                     session.scenario.duration,
                     session.scenario.difficulty
            ORDER BY AVG(message.pronunciationScore) ASC,
                     MAX(message.createdAt) DESC
            """)
    List<SpeakingScenarioScoreProjection> findWeakestDailyScenarioByUserId(
            @Param("userId") Long userId,
            Pageable pageable
    );

    @Query("""
            SELECT speakingSession.scenario.id AS scenarioId,
                   speakingSession.scenario.title AS topic,
                   speakingSession.scenario.description AS description,
                   COUNT(DISTINCT speakingSession.user.id) AS learnerCount
            FROM SpeakingSession speakingSession
            WHERE speakingSession.scenario.active = true
            GROUP BY speakingSession.scenario.id,
                     speakingSession.scenario.title,
                     speakingSession.scenario.description
            ORDER BY COUNT(DISTINCT speakingSession.user.id) DESC,
                     speakingSession.scenario.id ASC
            """)
    List<SpeakingLeaderboardProjection> findSpeakingLeaderboard(Pageable pageable);
}
