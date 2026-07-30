package com.englishlearningcopilot.backend.entity;

/**
 * Future queued speaking-turn lifecycle. Auxiliary ISE and TTS work remains
 * outside this user-visible response state machine.
 */
public enum SpeakingTurnTaskStatus {
    PENDING,
    TRANSCRIBING,
    GENERATING_REPLY,
    REPLY_READY,
    TRANSCRIPTION_FAILED,
    REPLY_FAILED
}
