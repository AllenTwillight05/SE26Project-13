package com.englishlearningcopilot.backend.service;

import com.englishlearningcopilot.backend.entity.LearningProgressOutboxEvent;
import com.englishlearningcopilot.backend.repository.LearningProgressOutboxEventRepository;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LearningProgressOutboxService {

    public static final String PRACTICE_TYPE_VOCABULARY = "VOCABULARY";
    public static final String PRACTICE_TYPE_GRAMMAR = "GRAMMAR";

    private static final int BATCH_SIZE = 50;
    private static final long LEASE_SECONDS = 30;

    private final LearningProgressOutboxEventRepository eventRepository;

    public LearningProgressOutboxService(LearningProgressOutboxEventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    /**
     * Joins the caller's score transaction, so a returned score always has a
     * durable statistics event to be processed later.
     */
    @Transactional
    public void enqueueVocabularyPractice(Long userId, Long vocabularyId) {
        enqueue(userId, PRACTICE_TYPE_VOCABULARY, String.valueOf(vocabularyId));
    }

    /**
     * Joins the caller's answer-result transaction for grammar practice.
     */
    @Transactional
    public void enqueueGrammarPractice(Long userId, Integer grammarQuestionId) {
        enqueue(userId, PRACTICE_TYPE_GRAMMAR, String.valueOf(grammarQuestionId));
    }

    @Transactional(readOnly = true)
    public List<Long> findReadyEventIds(Instant now) {
        return eventRepository.findReadyEventIds(now, PageRequest.of(0, BATCH_SIZE));
    }

    @Transactional
    public PracticeEvent claimReadyEvent(Long eventId, Instant now) {
        if (eventRepository.claimIfReady(eventId, now, now.plusSeconds(LEASE_SECONDS)) == 0) {
            return null;
        }

        LearningProgressOutboxEvent event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalStateException("Claimed outbox event was not found."));
        return new PracticeEvent(
                event.getId(),
                event.getUserId(),
                event.getPlanDate(),
                event.getPracticeType(),
                event.getItemId(),
                event.getAttempts()
        );
    }

    @Transactional
    public void markCompleted(Long eventId, Instant now) {
        eventRepository.markCompleted(eventId, now);
    }

    @Transactional
    public void reschedule(Long eventId, int attempts, String error, Instant now) {
        long delaySeconds = Math.min(60, Math.max(1, attempts));
        eventRepository.reschedule(eventId, now.plusSeconds(delaySeconds), truncate(error), now);
    }

    private void enqueue(Long userId, String practiceType, String itemId) {
        eventRepository.insertIfAbsent(userId, LocalDate.now(), practiceType, itemId);
    }

    private String truncate(String error) {
        if (error == null || error.isBlank()) {
            return "Unknown outbox processing failure.";
        }
        return error.length() <= 500 ? error : error.substring(0, 500);
    }

    public record PracticeEvent(
            Long id,
            Long userId,
            LocalDate planDate,
            String practiceType,
            String itemId,
            int attempts
    ) {
    }
}
