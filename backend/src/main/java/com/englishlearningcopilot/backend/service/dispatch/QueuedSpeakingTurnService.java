package com.englishlearningcopilot.backend.service.dispatch;

import com.englishlearningcopilot.backend.dto.SpeakingMessageResponse;
import com.englishlearningcopilot.backend.dto.SpeakingSessionResponse;
import com.englishlearningcopilot.backend.dto.SpeakingTurnResponse;
import com.englishlearningcopilot.backend.dto.SpeakingTurnTaskResponse;
import com.englishlearningcopilot.backend.entity.SpeakingMessage;
import com.englishlearningcopilot.backend.entity.SpeakingMessageSender;
import com.englishlearningcopilot.backend.entity.SpeakingSession;
import com.englishlearningcopilot.backend.entity.SpeakingSessionStatus;
import com.englishlearningcopilot.backend.entity.SpeakingTurnTask;
import com.englishlearningcopilot.backend.entity.SpeakingTurnTaskStatus;
import com.englishlearningcopilot.backend.exception.BadRequestException;
import com.englishlearningcopilot.backend.exception.ResourceNotFoundException;
import com.englishlearningcopilot.backend.repository.SpeakingMessageRepository;
import com.englishlearningcopilot.backend.repository.SpeakingSessionRepository;
import com.englishlearningcopilot.backend.repository.SpeakingTurnTaskRepository;
import com.englishlearningcopilot.backend.service.SpeakingAudioStorageService;
import java.io.IOException;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * Queued-mode HTTP boundary. It only persists durable input and returns; ASR
 * and LLM work happens in {@link SpeakingTurnTaskWorker} outside this request.
 */
public class QueuedSpeakingTurnService {

    private final SpeakingTurnTaskService taskService;
    private final SpeakingTurnTaskRepository taskRepository;
    private final SpeakingSessionRepository sessionRepository;
    private final SpeakingMessageRepository messageRepository;
    private final SpeakingAudioStorageService audioStorageService;

    public QueuedSpeakingTurnService(
            SpeakingTurnTaskService taskService,
            SpeakingTurnTaskRepository taskRepository,
            SpeakingSessionRepository sessionRepository,
            SpeakingMessageRepository messageRepository,
            SpeakingAudioStorageService audioStorageService
    ) {
        this.taskService = taskService;
        this.taskRepository = taskRepository;
        this.sessionRepository = sessionRepository;
        this.messageRepository = messageRepository;
        this.audioStorageService = audioStorageService;
    }

    @Transactional
    public SpeakingTurnTaskResponse submit(
            String username,
            Long sessionId,
            MultipartFile audio,
            Long durationMs,
            String attemptId
    ) {
        SpeakingTurnTask existing = findExistingOwnedTask(username, sessionId, attemptId);
        if (existing != null) {
            return toResponse(existing);
        }

        byte[] audioBytes;
        try {
            audioBytes = audio.getBytes();
        } catch (IOException e) {
            throw new BadRequestException("Failed to read uploaded audio file.");
        }
        if (audioBytes.length == 0) {
            throw new BadRequestException("The submitted recording is empty.");
        }

        // The first request creates the task. A sequential browser retry then
        // returns that exact task without uploading or invoking providers again.
        SpeakingTurnTask task = createTaskForUpload(username, sessionId, attemptId);
        if (task.getAudioUrl() != null) {
            return toResponse(task);
        }
        String audioUrl = audioStorageService.save(sessionId, task.getId(), audioBytes, extensionOf(audio.getOriginalFilename()));
        return attachAudio(task.getId(), audioUrl, audio.getOriginalFilename(), normalizeDurationMs(durationMs));
    }

    @Transactional(readOnly = true)
    public SpeakingTurnTaskResponse getStatus(String username, Long sessionId, Long taskId) {
        SpeakingTurnTask task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Speaking turn task was not found."));
        requireOwnedSession(username, sessionId);
        if (!task.getSessionId().equals(sessionId)) {
            throw new ResourceNotFoundException("Speaking turn task was not found.");
        }
        return toResponse(task);
    }

    @Transactional
    protected SpeakingTurnTask createTaskForUpload(String username, Long sessionId, String attemptId) {
        requireActiveOwnedSession(username, sessionId);
        SpeakingTurnTask task = taskService.createPendingTask(sessionId, null, attemptId);
        return taskRepository.findById(task.getId()).orElseThrow();
    }

    @Transactional
    protected SpeakingTurnTaskResponse attachAudio(Long taskId, String audioUrl, String originalFilename, Long durationMs) {
        SpeakingTurnTask task = taskRepository.findByIdForUpdate(taskId).orElseThrow();
        if (task.getAudioUrl() == null) {
            task.setAudioUrl(audioUrl);
            task.setOriginalFilename(originalFilename);
            task.setDurationMs(durationMs);
        }
        return toResponse(task);
    }

    @Transactional(readOnly = true)
    protected SpeakingTurnTask findExistingOwnedTask(String username, Long sessionId, String attemptId) {
        validateAttemptId(attemptId);
        SpeakingTurnTask task = taskRepository.findBySessionIdAndAttemptId(sessionId, attemptId.trim()).orElse(null);
        if (task != null) {
            requireOwnedSession(username, sessionId);
        }
        return task;
    }

    private SpeakingTurnTaskResponse toResponse(SpeakingTurnTask task) {
        SpeakingTurnResponse turn = null;
        if (task.getStatus() == SpeakingTurnTaskStatus.REPLY_READY
                && task.getUserMessageId() != null && task.getAgentMessageId() != null) {
            SpeakingMessage userMessage = messageRepository.findById(task.getUserMessageId()).orElse(null);
            SpeakingMessage agentMessage = messageRepository.findById(task.getAgentMessageId()).orElse(null);
            SpeakingSession session = sessionRepository.findById(task.getSessionId()).orElse(null);
            if (userMessage != null && agentMessage != null && session != null) {
                List<SpeakingMessageResponse> messages = messageRepository
                        .findBySessionIdOrderByTurnIndexAscCreatedAtAsc(session.getId()).stream()
                        .map(SpeakingMessageResponse::from)
                        .toList();
                turn = new SpeakingTurnResponse(
                        SpeakingMessageResponse.from(userMessage),
                        SpeakingMessageResponse.from(agentMessage),
                        null,
                        SpeakingSessionResponse.from(session, messages)
                );
            }
        }
        return new SpeakingTurnTaskResponse(
                task.getId(), task.getAttemptId(), task.getStatus().name(), task.getAttemptCount(),
                task.getUpdatedAt(), turn, errorCode(task.getStatus())
        );
    }

    private String errorCode(SpeakingTurnTaskStatus status) {
        return switch (status) {
            case TRANSCRIPTION_FAILED -> "TRANSCRIPTION_FAILED";
            case REPLY_FAILED -> "REPLY_FAILED";
            default -> null;
        };
    }

    private SpeakingSession requireOwnedSession(String username, Long sessionId) {
        SpeakingSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Speaking session was not found."));
        if (!session.getUser().getUsername().equals(username)) {
            throw new org.springframework.security.access.AccessDeniedException("This speaking session belongs to another user.");
        }
        return session;
    }

    private SpeakingSession requireActiveOwnedSession(String username, Long sessionId) {
        SpeakingSession session = requireOwnedSession(username, sessionId);
        if (session.getStatus() != SpeakingSessionStatus.ACTIVE) {
            throw new BadRequestException("Speaking session is not active.");
        }
        return session;
    }

    private void validateAttemptId(String attemptId) {
        if (attemptId == null || attemptId.trim().isBlank() || attemptId.trim().length() > 64) {
            throw new BadRequestException("A valid attemptId is required for queued speaking.");
        }
    }

    private Long normalizeDurationMs(Long durationMs) {
        return durationMs == null || durationMs <= 0 ? null : durationMs;
    }

    private String extensionOf(String originalFilename) {
        if (originalFilename == null) {
            return "webm";
        }
        int dot = originalFilename.lastIndexOf('.');
        return dot >= 0 && dot < originalFilename.length() - 1 ? originalFilename.substring(dot + 1) : "webm";
    }
}
