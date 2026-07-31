package com.englishlearningcopilot.backend.controller;

import com.englishlearningcopilot.backend.dto.SpeakingTurnResponse;
import com.englishlearningcopilot.backend.service.SpeakingService;
import java.security.Principal;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/** The established direct-call endpoint, registered only in inline mode. */
@RestController
@RequestMapping("/api/speaking")
@ConditionalOnProperty(name = "speaking.dispatch.mode", havingValue = "inline", matchIfMissing = true)
public class InlineSpeakingMessageController {

    private final SpeakingService speakingService;

    public InlineSpeakingMessageController(SpeakingService speakingService) {
        this.speakingService = speakingService;
    }

    @PostMapping(value = "/sessions/{sessionId}/messages", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public SpeakingTurnResponse submitRecording(
            Principal principal,
            @PathVariable Long sessionId,
            @RequestParam("audio") MultipartFile audio,
            @RequestParam(value = "durationMs", required = false) Long durationMs
    ) {
        // Additional multipart fields, including a queued-mode attemptId, are ignored by Spring.
        return speakingService.submitRecording(principal.getName(), sessionId, audio, durationMs);
    }
}
