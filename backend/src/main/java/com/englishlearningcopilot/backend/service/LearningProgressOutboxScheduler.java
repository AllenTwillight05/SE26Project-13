package com.englishlearningcopilot.backend.service;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(
        name = "app.learning-progress-outbox.scheduler-enabled",
        havingValue = "true",
        matchIfMissing = true
)
public class LearningProgressOutboxScheduler {

    private final LearningProgressOutboxProcessor processor;

    public LearningProgressOutboxScheduler(LearningProgressOutboxProcessor processor) {
        this.processor = processor;
    }

    @Scheduled(fixedDelayString = "${app.learning-progress-outbox.poll-interval-ms:200}")
    public void processReadyEvents() {
        processor.processReadyEvents();
    }
}
