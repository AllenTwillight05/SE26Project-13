package com.englishlearningcopilot.backend.dto;

import com.englishlearningcopilot.backend.entity.SpeakingMessage;
import java.time.Instant;

public record SpeakingMessageResponse(
        Long id,
        String sender,
        String content,
        String spokenText,
        String audioUrl,
        boolean audioPending,
        boolean autoPlay,
        boolean textOnly,
        String transcribedText,
        Double pronunciationScore,
        String pronunciationDetail,
        String instantTip,
        int turnIndex,
        Instant createdAt
) {

    public static SpeakingMessageResponse from(SpeakingMessage message) {
        return new SpeakingMessageResponse(
                message.getId(),
                message.getSender().name(),
                message.getContent(),
                message.getSender().name().equals("AGENT") ? message.getSpokenText() : null,
                message.getAudioUrl(),
                message.getSender().name().equals("AGENT") && message.isAudioPending(),
                message.getSender().name().equals("AGENT")
                        && (message.isAudioPending() || (message.getAudioUrl() != null && !message.getAudioUrl().isBlank())),
                message.getSender().name().equals("AGENT")
                        && !message.isAudioPending()
                        && (message.getSpokenText() == null || message.getSpokenText().isBlank()),
                message.getTranscribedText(),
                message.getPronunciationScore(),
                message.getPronunciationDetail(),
                message.getInstantTip(),
                message.getTurnIndex(),
                message.getCreatedAt()
        );
    }
}
