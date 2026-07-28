package com.englishlearningcopilot.backend.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LearningProgressOutboxProcessorTest {

    @Mock
    private LearningProgressOutboxService outboxService;

    @Mock
    private LearningPlanService learningPlanService;

    @InjectMocks
    private LearningProgressOutboxProcessor processor;

    @Test
    void appliesClaimedEventThenMarksItCompleted() {
        LearningProgressOutboxService.PracticeEvent event = event(12L, 1);
        when(outboxService.findReadyEventIds(any(Instant.class))).thenReturn(List.of(12L));
        when(outboxService.claimReadyEvent(eq(12L), any(Instant.class))).thenReturn(event);

        processor.processReadyEvents();

        verify(learningPlanService).recordOutboxPractice(
                7L, LocalDate.of(2026, 7, 28), "VOCABULARY", "10"
        );
        verify(outboxService).markCompleted(eq(12L), any(Instant.class));
    }

    @Test
    void reschedulesClaimedEventWhenStatisticsWriteFails() {
        LearningProgressOutboxService.PracticeEvent event = event(12L, 3);
        when(outboxService.claimReadyEvent(eq(12L), any(Instant.class))).thenReturn(event);
        doThrow(new IllegalStateException("database unavailable"))
                .when(learningPlanService)
                .recordOutboxPractice(7L, LocalDate.of(2026, 7, 28), "VOCABULARY", "10");

        processor.processEvent(12L);

        verify(outboxService).reschedule(eq(12L), eq(3), eq("database unavailable"), any(Instant.class));
    }

    private static LearningProgressOutboxService.PracticeEvent event(Long id, int attempts) {
        return new LearningProgressOutboxService.PracticeEvent(
                id,
                7L,
                LocalDate.of(2026, 7, 28),
                "VOCABULARY",
                "10",
                attempts
        );
    }
}
