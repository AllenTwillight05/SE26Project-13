package com.englishlearningcopilot.backend.config;

import com.englishlearningcopilot.backend.repository.SpeakingTurnTaskRepository;
import com.englishlearningcopilot.backend.repository.SpeakingSessionRepository;
import com.englishlearningcopilot.backend.repository.SpeakingMessageRepository;
import com.englishlearningcopilot.backend.service.SpeakingAgentAudioSynthesisService;
import com.englishlearningcopilot.backend.service.SpeakingAudioStorageService;
import com.englishlearningcopilot.backend.service.SpeakingPronunciationEvaluationService;
import com.englishlearningcopilot.backend.service.agent.SpeakingAgentClient;
import com.englishlearningcopilot.backend.service.dispatch.SpeakingDispatchProperties;
import com.englishlearningcopilot.backend.service.dispatch.SpeakingTurnTaskService;
import com.englishlearningcopilot.backend.service.dispatch.QueuedSpeakingTurnService;
import com.englishlearningcopilot.backend.service.dispatch.SpeakingTurnTaskWorker;
import com.englishlearningcopilot.backend.service.speech.AsrService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Queue execution is deliberately not enabled by default. Existing speaking
 * requests continue through the inline implementation until a dispatcher and
 * client protocol are explicitly introduced behind this mode.
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = "speaking.dispatch.mode", havingValue = "queued")
@EnableConfigurationProperties(SpeakingDispatchProperties.class)
public class SpeakingQueuedDispatchConfiguration {

    @Bean
    public SpeakingTurnTaskService speakingTurnTaskService(SpeakingTurnTaskRepository taskRepository) {
        return new SpeakingTurnTaskService(taskRepository);
    }

    @Bean
    public QueuedSpeakingTurnService queuedSpeakingTurnService(
            SpeakingTurnTaskService taskService,
            SpeakingTurnTaskRepository taskRepository,
            SpeakingSessionRepository sessionRepository,
            SpeakingMessageRepository messageRepository,
            SpeakingAudioStorageService audioStorageService
    ) {
        return new QueuedSpeakingTurnService(taskService, taskRepository, sessionRepository, messageRepository, audioStorageService);
    }

    @Bean
    public SpeakingTurnTaskWorker speakingTurnTaskWorker(
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
        return new SpeakingTurnTaskWorker(
                taskRepository, sessionRepository, messageRepository, audioStorageService, asrService, agentClient,
                audioSynthesisService, pronunciationEvaluationService, properties, transactionManager
        );
    }
}
