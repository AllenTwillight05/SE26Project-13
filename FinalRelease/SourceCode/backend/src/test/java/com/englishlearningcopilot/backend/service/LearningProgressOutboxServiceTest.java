package com.englishlearningcopilot.backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.englishlearningcopilot.backend.entity.LearningProgressOutboxEvent;
import com.englishlearningcopilot.backend.repository.LearningProgressOutboxEventRepository;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class LearningProgressOutboxServiceTest {

    @Mock
    private LearningProgressOutboxEventRepository eventRepository;

    private LearningProgressOutboxService outboxService;

    @BeforeEach
    void setUp() {
        outboxService = new LearningProgressOutboxService(eventRepository);
    }

    @Test
    void enqueueVocabularyPracticeStoresCurrentDayEvent() {
        outboxService.enqueueVocabularyPractice(7L, 10L);

        verify(eventRepository).insertIfAbsent(7L, LocalDate.now(), "VOCABULARY", "10");
    }

    @Test
    void enqueueGrammarPracticeStoresCurrentDayEvent() {
        outboxService.enqueueGrammarPractice(7L, 3);

        verify(eventRepository).insertIfAbsent(7L, LocalDate.now(), "GRAMMAR", "3");
    }

    @Test
    void findReadyEventIdsUsesFixedBatchSize() {
        Instant now = Instant.now();

        outboxService.findReadyEventIds(now);

        verify(eventRepository).findReadyEventIds(now, PageRequest.of(0, 50));
    }

    @Test
    void claimReadyEventReturnsNullWhenLeaseIsNotAcquired() {
        Instant now = Instant.now();
        when(eventRepository.claimIfReady(12L, now, now.plusSeconds(30))).thenReturn(0);

        assertThat(outboxService.claimReadyEvent(12L, now)).isNull();
    }

    @Test
    void claimReadyEventReturnsPracticeEventWhenLeaseIsAcquired() {
        Instant now = Instant.now();
        LearningProgressOutboxEvent event = event(12L, 2);
        when(eventRepository.claimIfReady(12L, now, now.plusSeconds(30))).thenReturn(1);
        when(eventRepository.findById(12L)).thenReturn(Optional.of(event));

        LearningProgressOutboxService.PracticeEvent claimed = outboxService.claimReadyEvent(12L, now);

        assertThat(claimed.id()).isEqualTo(12L);
        assertThat(claimed.userId()).isEqualTo(7L);
        assertThat(claimed.practiceType()).isEqualTo("VOCABULARY");
        assertThat(claimed.itemId()).isEqualTo("10");
        assertThat(claimed.attempts()).isEqualTo(2);
    }

    @Test
    void claimReadyEventFailsIfClaimedEventDisappears() {
        Instant now = Instant.now();
        when(eventRepository.claimIfReady(12L, now, now.plusSeconds(30))).thenReturn(1);
        when(eventRepository.findById(12L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> outboxService.claimReadyEvent(12L, now))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Claimed outbox event was not found.");
    }

    @Test
    void markCompletedDelegatesToRepository() {
        Instant now = Instant.now();

        outboxService.markCompleted(12L, now);

        verify(eventRepository).markCompleted(12L, now);
    }

    @Test
    void rescheduleUsesMinimumDelayAndFallbackErrorMessage() {
        Instant now = Instant.now();

        outboxService.reschedule(12L, 0, " ", now);

        verify(eventRepository).reschedule(
                eq(12L),
                eq(now.plusSeconds(1)),
                eq("Unknown outbox processing failure."),
                eq(now)
        );
    }

    @Test
    void rescheduleUsesFallbackErrorMessageForNullError() {
        Instant now = Instant.now();

        outboxService.reschedule(12L, 1, null, now);

        verify(eventRepository).reschedule(
                eq(12L),
                eq(now.plusSeconds(1)),
                eq("Unknown outbox processing failure."),
                eq(now)
        );
    }

    @Test
    void rescheduleKeepsShortErrorMessages() {
        Instant now = Instant.now();

        outboxService.reschedule(12L, 5, "short error", now);

        verify(eventRepository).reschedule(12L, now.plusSeconds(5), "short error", now);
    }

    @Test
    void rescheduleCapsDelayAndTruncatesLongErrorMessages() {
        Instant now = Instant.now();
        String longError = "x".repeat(600);

        outboxService.reschedule(12L, 120, longError, now);

        verify(eventRepository).reschedule(eq(12L), eq(now.plusSeconds(60)), any(String.class), eq(now));
    }

    private static LearningProgressOutboxEvent event(Long id, int attempts) {
        LearningProgressOutboxEvent event = new LearningProgressOutboxEvent();
        ReflectionTestUtils.setField(event, "id", id);
        ReflectionTestUtils.setField(event, "userId", 7L);
        ReflectionTestUtils.setField(event, "planDate", LocalDate.of(2026, 7, 28));
        ReflectionTestUtils.setField(event, "practiceType", "VOCABULARY");
        ReflectionTestUtils.setField(event, "itemId", "10");
        ReflectionTestUtils.setField(event, "attempts", attempts);
        return event;
    }
}
