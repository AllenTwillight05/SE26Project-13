package com.englishlearningcopilot.backend.controller;

import com.englishlearningcopilot.backend.dto.SpeakingTurnTaskResponse;
import com.englishlearningcopilot.backend.service.dispatch.QueuedSpeakingTurnService;
import java.security.Principal;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/** Queued-mode contract, absent from the current inline application context. */
@RestController
@RequestMapping("/api/speaking")
@ConditionalOnProperty(name = "speaking.dispatch.mode", havingValue = "queued")
public class QueuedSpeakingMessageController {

    private final QueuedSpeakingTurnService queuedSpeakingTurnService;

    public QueuedSpeakingMessageController(QueuedSpeakingTurnService queuedSpeakingTurnService) {
        this.queuedSpeakingTurnService = queuedSpeakingTurnService;
    }

    @PostMapping(value = "/sessions/{sessionId}/messages", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SpeakingTurnTaskResponse> submitRecording(
            Principal principal,
            @PathVariable Long sessionId,
            @RequestParam("audio") MultipartFile audio,
            @RequestParam(value = "durationMs", required = false) Long durationMs,
            @RequestParam(value = "attemptId", required = false) String attemptId
    ) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(queuedSpeakingTurnService.submit(principal.getName(), sessionId, audio, durationMs, attemptId));
    }

    @GetMapping("/sessions/{sessionId}/turn-tasks/{taskId}")
    public SpeakingTurnTaskResponse getTurnTask(
            Principal principal,
            @PathVariable Long sessionId,
            @PathVariable Long taskId
    ) {
        return queuedSpeakingTurnService.getStatus(principal.getName(), sessionId, taskId);
    }
}
