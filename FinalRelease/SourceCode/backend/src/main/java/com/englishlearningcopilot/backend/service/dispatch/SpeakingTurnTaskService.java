package com.englishlearningcopilot.backend.service.dispatch;

import com.englishlearningcopilot.backend.entity.SpeakingTurnTask;
import com.englishlearningcopilot.backend.repository.SpeakingTurnTaskRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Used only by the future queued dispatcher. It is intentionally not a
 * component, so inline mode does not create the service or touch this table.
 */
public class SpeakingTurnTaskService {

    private final SpeakingTurnTaskRepository taskRepository;

    public SpeakingTurnTaskService(SpeakingTurnTaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Transactional
    public SpeakingTurnTask createPendingTask(Long sessionId, Long userMessageId, String attemptId) {
        if (sessionId == null || sessionId <= 0) {
            throw new IllegalArgumentException("Speaking turn task requires a session id.");
        }
        String normalizedAttemptId = attemptId == null ? "" : attemptId.trim();
        if (normalizedAttemptId.isBlank() || normalizedAttemptId.length() > 64) {
            throw new IllegalArgumentException("Speaking turn task requires an attemptId of at most 64 characters.");
        }

        taskRepository.insertIfAbsent(sessionId, userMessageId, normalizedAttemptId);
        return taskRepository.findBySessionIdAndAttemptId(sessionId, normalizedAttemptId)
                .orElseThrow(() -> new IllegalStateException("Speaking turn task was not created."));
    }
}
