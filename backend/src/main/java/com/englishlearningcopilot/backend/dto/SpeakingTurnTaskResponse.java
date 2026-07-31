package com.englishlearningcopilot.backend.dto;

import java.time.Instant;

/**
 * The queued-mode polling contract. {@code turn} is set only once the agent
 * reply is committed, so a browser retry never needs to infer completion from
 * transient task state.
 */
public record SpeakingTurnTaskResponse(
        Long taskId,
        String attemptId,
        String status,
        int attemptCount,
        Instant updatedAt,
        SpeakingTurnResponse turn,
        String errorCode
) {
}
