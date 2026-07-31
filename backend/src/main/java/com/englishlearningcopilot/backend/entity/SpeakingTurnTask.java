package com.englishlearningcopilot.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A durable envelope for a future queued speaking turn. The current inline
 * request flow does not create or read these rows.
 */
@Entity
@Table(
        name = "speaking_turn_tasks",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_speaking_turn_task_attempt",
                columnNames = {"session_id", "attempt_id"}
        ),
        indexes = @Index(
                name = "idx_speaking_turn_task_ready",
                columnList = "status, available_at"
        )
)
@Getter
@Setter
@NoArgsConstructor
public class SpeakingTurnTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(name = "session_id", nullable = false)
    private Long sessionId;

    @Column(name = "user_message_id")
    private Long userMessageId;

    @Column(name = "agent_message_id")
    private Long agentMessageId;

    @Column(name = "audio_url", length = 500)
    private String audioUrl;

    @Column(name = "original_filename", length = 255)
    private String originalFilename;

    @Column(name = "duration_ms")
    private Long durationMs;

    @Column(name = "attempt_id", nullable = false, length = 64)
    private String attemptId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private SpeakingTurnTaskStatus status = SpeakingTurnTaskStatus.PENDING;

    @Column(name = "assigned_asr_route", length = 100)
    private String assignedAsrRoute;

    @Column(name = "assigned_llm_route", length = 100)
    private String assignedLlmRoute;

    @Column(name = "attempt_count", nullable = false)
    private int attemptCount;

    @Column(name = "available_at", nullable = false)
    private Instant availableAt;

    @Column(name = "last_error", length = 500)
    private String lastError;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Setter(AccessLevel.NONE)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    @Setter(AccessLevel.NONE)
    private Instant updatedAt;

    @Version
    @Column(nullable = false)
    private Long version;

    @PrePersist
    void prePersist() {
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
        if (availableAt == null) {
            availableAt = now;
        }
        if (status == null) {
            status = SpeakingTurnTaskStatus.PENDING;
        }
        if (version == null) {
            version = 0L;
        }
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = Instant.now();
    }
}
