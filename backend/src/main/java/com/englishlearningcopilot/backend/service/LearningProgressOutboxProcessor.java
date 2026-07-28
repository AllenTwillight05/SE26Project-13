package com.englishlearningcopilot.backend.service;

import java.time.Instant;
import org.springframework.stereotype.Service;

/**
 * Processes durable statistics events outside the score request transaction.
 * Each phase commits independently so a restart can safely retry from the
 * stored event without losing the work item.
 */
@Service
public class LearningProgressOutboxProcessor {

    private final LearningProgressOutboxService outboxService;
    private final LearningPlanService learningPlanService;

    public LearningProgressOutboxProcessor(
            LearningProgressOutboxService outboxService,
            LearningPlanService learningPlanService
    ) {
        this.outboxService = outboxService;
        this.learningPlanService = learningPlanService;
    }

    public void processReadyEvents() {
        for (Long eventId : outboxService.findReadyEventIds(Instant.now())) {
            processEvent(eventId);
        }
    }

    public void processEvent(Long eventId) {
        LearningProgressOutboxService.PracticeEvent event = outboxService.claimReadyEvent(eventId, Instant.now());
        if (event == null) {
            return;
        }

        try {
            learningPlanService.recordOutboxPractice(
                    event.userId(),
                    event.planDate(),
                    event.practiceType(),
                    event.itemId()
            );
            outboxService.markCompleted(event.id(), Instant.now());
        } catch (RuntimeException ex) {
            outboxService.reschedule(event.id(), event.attempts(), ex.getMessage(), Instant.now());
        }
    }
}
