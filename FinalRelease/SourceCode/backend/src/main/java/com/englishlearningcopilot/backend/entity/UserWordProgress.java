package com.englishlearningcopilot.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.Instant;

@Entity
@Table(
        name = "user_word_progress",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_user_question",
                columnNames = {"user_id", "question_id", "question_type"}
        ),
        indexes = @Index(
                name = "idx_user_type_due",
                columnList = "user_id, question_type, due"
        )
)
public class UserWordProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "question_id", nullable = false, length = 64)
    private String questionId;

    @Column(name = "question_type", nullable = false, length = 20)
    private String questionType = "vocabulary";

    private Double difficulty = 2.5;

    private Double stability = 2.5;

    @Column(name = "`interval`")
    private Integer interval = 0;

    private Integer reps = 0;

    private Integer lapses = 0;

    private Integer state = 0;

    @Column(nullable = false)
    private Instant due;

    @Column(name = "last_review")
    private Instant lastReview;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    void prePersist() {
        Instant now = Instant.now();
        if (due == null) {
            due = now;
        }
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public Double getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Double difficulty) {
        this.difficulty = difficulty;
    }

    public Double getStability() {
        return stability;
    }

    public void setStability(Double stability) {
        this.stability = stability;
    }

    public Integer getInterval() {
        return interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    public Integer getReps() {
        return reps;
    }

    public void setReps(Integer reps) {
        this.reps = reps;
    }

    public Integer getLapses() {
        return lapses;
    }

    public void setLapses(Integer lapses) {
        this.lapses = lapses;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Instant getDue() {
        return due;
    }

    public void setDue(Instant due) {
        this.due = due;
    }

    public Instant getLastReview() {
        return lastReview;
    }

    public void setLastReview(Instant lastReview) {
        this.lastReview = lastReview;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
