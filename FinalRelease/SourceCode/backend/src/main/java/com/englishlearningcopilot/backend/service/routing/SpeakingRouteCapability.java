package com.englishlearningcopilot.backend.service.routing;

/**
 * External capabilities are scheduled independently because their quotas and
 * failure modes are not interchangeable.
 */
public enum SpeakingRouteCapability {
    LLM,
    ASR,
    ISE,
    TTS
}
