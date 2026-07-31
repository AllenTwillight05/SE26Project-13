package com.englishlearningcopilot.backend.service.dispatch;

import com.englishlearningcopilot.backend.entity.SpeakingMessage;
import com.englishlearningcopilot.backend.entity.SpeakingMessageSender;
import com.englishlearningcopilot.backend.entity.SpeakingScenario;
import com.englishlearningcopilot.backend.entity.SpeakingSession;
import com.englishlearningcopilot.backend.entity.SpeakingTurnTask;
import com.englishlearningcopilot.backend.entity.SpeakingTurnTaskStatus;
import com.englishlearningcopilot.backend.repository.SpeakingMessageRepository;
import com.englishlearningcopilot.backend.repository.SpeakingSessionRepository;
import com.englishlearningcopilot.backend.repository.SpeakingTurnTaskRepository;
import com.englishlearningcopilot.backend.service.SpeakingAgentAudioSynthesisService;
import com.englishlearningcopilot.backend.service.SpeakingAudioStorageService;
import com.englishlearningcopilot.backend.service.SpeakingPronunciationEvaluationService;
import com.englishlearningcopilot.backend.service.agent.SpeakingAgentClient;
import com.englishlearningcopilot.backend.service.agent.SpeakingAgentReply;
import com.englishlearningcopilot.backend.service.speech.AsrService;
import com.englishlearningcopilot.backend.service.speech.EnglishSpeechText;
import java.time.Instant;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Durable queued-mode worker. Every provider invocation is deliberately made
 * outside a database transaction. Small state transitions use row locks so a
 * second application instance cannot process the same task or reorder turns
 * within one session.
 */
public class SpeakingTurnTaskWorker {

    private static final Logger log = LoggerFactory.getLogger(SpeakingTurnTaskWorker.class);

    private final SpeakingTurnTaskRepository taskRepository;
    private final SpeakingSessionRepository sessionRepository;
    private final SpeakingMessageRepository messageRepository;
    private final SpeakingAudioStorageService audioStorageService;
    private final AsrService asrService;
    private final SpeakingAgentClient agentClient;
    private final SpeakingAgentAudioSynthesisService audioSynthesisService;
    private final SpeakingPronunciationEvaluationService pronunciationEvaluationService;
    private final SpeakingDispatchProperties properties;
    private final TransactionTemplate transactionTemplate;

    public SpeakingTurnTaskWorker(
            SpeakingTurnTaskRepository taskRepository,
            SpeakingSessionRepository sessionRepository,
            SpeakingMessageRepository messageRepository,
            SpeakingAudioStorageService audioStorageService,
            AsrService asrService,
            SpeakingAgentClient agentClient,
            SpeakingAgentAudioSynthesisService audioSynthesisService,
            SpeakingPronunciationEvaluationService pronunciationEvaluationService,
            SpeakingDispatchProperties properties,
            PlatformTransactionManager transactionManager
    ) {
        this.taskRepository = taskRepository;
        this.sessionRepository = sessionRepository;
        this.messageRepository = messageRepository;
        this.audioStorageService = audioStorageService;
        this.asrService = asrService;
        this.agentClient = agentClient;
        this.audioSynthesisService = audioSynthesisService;
        this.pronunciationEvaluationService = pronunciationEvaluationService;
        this.properties = properties;
        this.transactionTemplate = new TransactionTemplate(transactionManager);
    }

    @Scheduled(fixedDelayString = "${speaking.dispatch.poll-interval-ms:250}")
    public void processReadyTasks() {
        int maxTasks = Math.max(1, properties.getBatchSize());
        for (int count = 0; count < maxTasks; count++) {
            Long taskId = nextReadyTaskId();
            if (taskId == null) {
                return;
            }
            processTask(taskId);
        }
    }

    void processTask(Long taskId) {
        ClaimedTask claimed = claim(taskId);
        if (claimed == null) {
            return;
        }

        byte[] audio;
        String transcript;
        try {
            audio = audioStorageService.load(claimed.audioUrl());
            transcript = asrService.transcribe(audio, claimed.originalFilename());
        } catch (RuntimeException e) {
            retryOrFail(taskId, SpeakingTurnTaskStatus.TRANSCRIPTION_FAILED, e);
            return;
        }

        ReplyInput replyInput;
        try {
            replyInput = persistTranscription(taskId, transcript);
        } catch (RuntimeException e) {
            retryOrFail(taskId, SpeakingTurnTaskStatus.TRANSCRIPTION_FAILED, e);
            return;
        }

        SpeakingAgentReply reply;
        try {
            reply = agentClient.reply(
                    replyInput.scenario(),
                    replyInput.selectedTopic(),
                    replyInput.history(),
                    replyInput.transcript(),
                    replyInput.turnIndex()
            );
        } catch (RuntimeException e) {
            retryOrFail(taskId, SpeakingTurnTaskStatus.REPLY_FAILED, e);
            return;
        }

        try {
            CompletedTask completed = persistReply(taskId, reply, replyInput.chineseHelpTurn());
            if (completed.synthesizeAudio()) {
                audioSynthesisService.synthesizeAgentMessageAsync(completed.agentMessageId());
            }
            if (completed.evaluatePronunciation()) {
                pronunciationEvaluationService.evaluateUserMessageAsync(
                        completed.userMessageId(), audio, replyInput.transcript());
            }
        } catch (RuntimeException e) {
            retryOrFail(taskId, SpeakingTurnTaskStatus.REPLY_FAILED, e);
        }
    }

    protected Long nextReadyTaskId() {
        return transactionTemplate.execute(ignored -> nextReadyTaskIdInTransaction());
    }

    private Long nextReadyTaskIdInTransaction() {
        return taskRepository.findFirstByStatusAndAudioUrlIsNotNullAndAvailableAtLessThanEqualOrderByCreatedAtAsc(
                        SpeakingTurnTaskStatus.PENDING, Instant.now())
                .map(SpeakingTurnTask::getId)
                .orElse(null);
    }

    protected ClaimedTask claim(Long taskId) {
        return transactionTemplate.execute(ignored -> claimInTransaction(taskId));
    }

    private ClaimedTask claimInTransaction(Long taskId) {
        SpeakingTurnTask task = taskRepository.findByIdForUpdate(taskId).orElse(null);
        if (task == null || task.getStatus() != SpeakingTurnTaskStatus.PENDING || task.getAudioUrl() == null) {
            return null;
        }
        SpeakingSession session = sessionRepository.findByIdForUpdate(task.getSessionId()).orElse(null);
        if (session == null || hasEarlierUnfinishedTask(task)) {
            return null;
        }
        task.setStatus(SpeakingTurnTaskStatus.TRANSCRIBING);
        task.setAttemptCount(task.getAttemptCount() + 1);
        return new ClaimedTask(task.getAudioUrl(), task.getOriginalFilename());
    }

    protected ReplyInput persistTranscription(Long taskId, String transcript) {
        return transactionTemplate.execute(ignored -> persistTranscriptionInTransaction(taskId, transcript));
    }

    private ReplyInput persistTranscriptionInTransaction(Long taskId, String transcript) {
        SpeakingTurnTask task = taskRepository.findByIdForUpdate(taskId).orElseThrow();
        if (task.getStatus() != SpeakingTurnTaskStatus.TRANSCRIBING) {
            throw new IllegalStateException("Speaking turn task is no longer transcribing.");
        }
        SpeakingSession session = sessionRepository.findByIdForUpdate(task.getSessionId()).orElseThrow();
        SpeakingScenario scenario = session.getScenario();
        // Initialize the lazy scenario relation before the LLM call happens
        // outside this short persistence transaction.
        scenario.getId();
        boolean chineseHelpTurn = EnglishSpeechText.containsChineseCharacters(transcript);
        int turnIndex = chineseHelpTurn ? session.getCurrentTurn() : session.getCurrentTurn() + 1;

        SpeakingMessage userMessage = new SpeakingMessage();
        userMessage.setSession(session);
        userMessage.setSender(SpeakingMessageSender.USER);
        userMessage.setContent(transcript);
        userMessage.setTranscribedText(transcript);
        userMessage.setAudioUrl(task.getAudioUrl());
        userMessage.setDurationMs(task.getDurationMs());
        userMessage.setTurnIndex(turnIndex);
        userMessage = messageRepository.save(userMessage);

        task.setUserMessageId(userMessage.getId());
        task.setStatus(SpeakingTurnTaskStatus.GENERATING_REPLY);
        List<SpeakingMessage> history = messageRepository.findBySessionIdOrderByTurnIndexAscCreatedAtAsc(session.getId());
        return new ReplyInput(scenario, session.getSelectedTopic(), history, transcript, turnIndex, chineseHelpTurn);
    }

    protected CompletedTask persistReply(Long taskId, SpeakingAgentReply reply, boolean chineseHelpTurn) {
        return transactionTemplate.execute(ignored -> persistReplyInTransaction(taskId, reply, chineseHelpTurn));
    }

    private CompletedTask persistReplyInTransaction(Long taskId, SpeakingAgentReply reply, boolean chineseHelpTurn) {
        SpeakingTurnTask task = taskRepository.findByIdForUpdate(taskId).orElseThrow();
        if (task.getStatus() != SpeakingTurnTaskStatus.GENERATING_REPLY || task.getUserMessageId() == null) {
            throw new IllegalStateException("Speaking turn task is no longer generating a reply.");
        }
        SpeakingSession session = sessionRepository.findByIdForUpdate(task.getSessionId()).orElseThrow();
        SpeakingMessage userMessage = messageRepository.findById(task.getUserMessageId()).orElseThrow();

        SpeakingMessage agentMessage = new SpeakingMessage();
        agentMessage.setSession(session);
        agentMessage.setSender(SpeakingMessageSender.AGENT);
        agentMessage.setContent(reply.content());
        agentMessage.setSpokenText(chineseHelpTurn ? null : spokenText(reply));
        agentMessage.setInstantTip(reply.instantTip());
        agentMessage.setTurnIndex(userMessage.getTurnIndex());
        agentMessage.setAudioPending(!chineseHelpTurn);
        agentMessage = messageRepository.save(agentMessage);

        if (!chineseHelpTurn) {
            session.setCurrentTurn(session.getCurrentTurn() + 1);
        }
        task.setAgentMessageId(agentMessage.getId());
        task.setStatus(SpeakingTurnTaskStatus.REPLY_READY);
        return new CompletedTask(
                userMessage.getId(), agentMessage.getId(), !chineseHelpTurn,
                !chineseHelpTurn && EnglishSpeechText.isEligibleForPronunciationEvaluation(userMessage.getTranscribedText())
        );
    }

    protected void retryOrFail(Long taskId, SpeakingTurnTaskStatus finalFailure, RuntimeException error) {
        transactionTemplate.executeWithoutResult(ignored -> retryOrFailInTransaction(taskId, finalFailure, error));
    }

    private void retryOrFailInTransaction(Long taskId, SpeakingTurnTaskStatus finalFailure, RuntimeException error) {
        SpeakingTurnTask task = taskRepository.findByIdForUpdate(taskId).orElse(null);
        if (task == null || task.getStatus() == SpeakingTurnTaskStatus.REPLY_READY) {
            return;
        }
        task.setLastError(shortError(error));
        // Re-running ASR is safe before a USER message has been committed.
        // Re-running LLM after that point needs its own persisted reply-stage
        // retry state, so it remains terminal until that protocol is added.
        if (finalFailure == SpeakingTurnTaskStatus.TRANSCRIPTION_FAILED
                && task.getUserMessageId() == null
                && task.getAttemptCount() < Math.max(1, properties.getMaxAttempts())) {
            task.setStatus(SpeakingTurnTaskStatus.PENDING);
            task.setAvailableAt(Instant.now().plusMillis(Math.max(0, properties.getRetryDelayMs())));
        } else {
            task.setStatus(finalFailure);
        }
        log.warn("Queued speaking task failed: taskId={}, status={}, attempts={}",
                taskId, task.getStatus(), task.getAttemptCount(), error);
    }

    private boolean hasEarlierUnfinishedTask(SpeakingTurnTask candidate) {
        return taskRepository.findBySessionIdOrderByCreatedAtAscIdAsc(candidate.getSessionId()).stream()
                .takeWhile(task -> !task.getId().equals(candidate.getId()))
                .anyMatch(task -> task.getStatus() != SpeakingTurnTaskStatus.REPLY_READY);
    }

    private String spokenText(SpeakingAgentReply reply) {
        return reply.spokenText() != null && !reply.spokenText().isBlank() ? reply.spokenText() : reply.content();
    }

    private String shortError(RuntimeException error) {
        String message = error.getMessage() == null ? error.getClass().getSimpleName() : error.getMessage();
        return message.length() <= 500 ? message : message.substring(0, 500);
    }

    private record ClaimedTask(String audioUrl, String originalFilename) {
    }

    private record ReplyInput(
            SpeakingScenario scenario,
            String selectedTopic,
            List<SpeakingMessage> history,
            String transcript,
            int turnIndex,
            boolean chineseHelpTurn
    ) {
    }

    private record CompletedTask(
            Long userMessageId,
            Long agentMessageId,
            boolean synthesizeAudio,
            boolean evaluatePronunciation
    ) {
    }
}
