package com.englishlearningcopilot.backend.service.dispatch;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "speaking.dispatch")
public class SpeakingDispatchProperties {

    private SpeakingDispatchMode mode = SpeakingDispatchMode.INLINE;
    private int pollIntervalMs = 250;
    private int batchSize = 4;
    private int maxAttempts = 2;
    private long retryDelayMs = 1_000;

    public SpeakingDispatchMode getMode() {
        return mode;
    }

    public void setMode(SpeakingDispatchMode mode) {
        this.mode = mode == null ? SpeakingDispatchMode.INLINE : mode;
    }

    public int getPollIntervalMs() {
        return pollIntervalMs;
    }

    public void setPollIntervalMs(int pollIntervalMs) {
        this.pollIntervalMs = pollIntervalMs;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public void setMaxAttempts(int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    public long getRetryDelayMs() {
        return retryDelayMs;
    }

    public void setRetryDelayMs(long retryDelayMs) {
        this.retryDelayMs = retryDelayMs;
    }
}
