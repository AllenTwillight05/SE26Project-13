package com.englishlearningcopilot.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.Instant;
import java.time.LocalDate;

/**
 * Durable work item for practice statistics that are intentionally outside the
 * scoring request's response path.
 */
@Entity
@Table(
        name = "learning_progress_outbox_events",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_learning_progress_outbox_practice",
                columnNames = {"user_id", "plan_date", "practice_type", "item_id"}
        ),
        indexes = @Index(
                name = "idx_learning_progress_outbox_ready",
                columnList = "status, available_at"
        )
)
public class LearningProgressOutboxEvent {

    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_PROCESSING = "PROCESSING";
    public static final String STATUS_COMPLETED = "COMPLETED";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "plan_date", nullable = false)
    private LocalDate planDate;

    @Column(name = "practice_type", nullable = false, length = 20)
    private String practiceType;

    @Column(name = "item_id", nullable = false, length = 64)
    private String itemId;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(nullable = false)
    private int attempts;

    @Column(name = "available_at", nullable = false)
    private Instant availableAt;

    @Column(name = "locked_until")
    private Instant lockedUntil;

    @Column(name = "processed_at")
    private Instant processedAt;

    @Column(name = "last_error", length = 500)
    private String lastError;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public LocalDate getPlanDate() {
        return planDate;
    }

    public String getPracticeType() {
        return practiceType;
    }

    public String getItemId() {
        return itemId;
    }

    public String getStatus() {
        return status;
    }

    public int getAttempts() {
        return attempts;
    }

    public Instant getAvailableAt() {
        return availableAt;
    }

    public Instant getLockedUntil() {
        return lockedUntil;
    }

    public Instant getProcessedAt() {
        return processedAt;
    }

    public String getLastError() {
        return lastError;
    }
}
