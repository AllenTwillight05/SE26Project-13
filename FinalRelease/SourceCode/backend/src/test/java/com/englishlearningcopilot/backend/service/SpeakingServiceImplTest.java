package com.englishlearningcopilot.backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

import com.englishlearningcopilot.backend.dto.CreateSpeakingSessionRequest;
import com.englishlearningcopilot.backend.dto.SpeakingFeedbackResponse;
import com.englishlearningcopilot.backend.dto.SpeakingSessionResponse;
import com.englishlearningcopilot.backend.dto.SpeakingTurnResponse;
import com.englishlearningcopilot.backend.entity.AppUser;
import com.englishlearningcopilot.backend.entity.SpeakingMessage;
import com.englishlearningcopilot.backend.entity.SpeakingMessageSender;
import com.englishlearningcopilot.backend.entity.SpeakingScenario;
import com.englishlearningcopilot.backend.entity.SpeakingSession;
import com.englishlearningcopilot.backend.entity.SpeakingSessionStatus;
import com.englishlearningcopilot.backend.entity.UserRole;
import com.englishlearningcopilot.backend.exception.BadRequestException;
import com.englishlearningcopilot.backend.exception.ResourceNotFoundException;
import com.englishlearningcopilot.backend.repository.SpeakingMessageRepository;
import com.englishlearningcopilot.backend.repository.SpeakingScenarioRepository;
import com.englishlearningcopilot.backend.repository.SpeakingSessionRepository;
import com.englishlearningcopilot.backend.repository.UserRepository;
import com.englishlearningcopilot.backend.service.agent.SpeakingAgentClient;
import com.englishlearningcopilot.backend.service.agent.SpeakingAgentReply;
import com.englishlearningcopilot.backend.service.impl.SpeakingServiceImpl;
import com.englishlearningcopilot.backend.service.speech.AsrService;
import com.englishlearningcopilot.backend.service.speech.IseService;
import com.englishlearningcopilot.backend.service.speech.PronunciationScore;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class SpeakingServiceImplTest {

    @Mock
    private SpeakingScenarioRepository scenarioRepository;

    @Mock
    private SpeakingSessionRepository sessionRepository;

    @Mock
    private SpeakingMessageRepository messageRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SpeakingAgentClient agentClient;

    @Mock
    private AsrService asrService;

    @Mock
    private IseService iseService;

    @Mock
    private SpeakingAudioStorageService audioStorageService;

    @Mock
    private SpeakingAgentAudioSynthesisService agentAudioSynthesisService;

    @Mock
    private SpeakingPronunciationEvaluationService pronunciationEvaluationService;

    @Mock
    private ObjectMapper objectMapper;

    private SpeakingServiceImpl speakingService;

    @BeforeEach
    void setUp() {
        speakingService = new SpeakingServiceImpl(
                scenarioRepository,
                sessionRepository,
                messageRepository,
                userRepository,
                agentClient,
                asrService,
                iseService,
                audioStorageService,
                agentAudioSynthesisService,
                pronunciationEvaluationService,
                objectMapper,
                false,
                false
        );
    }

    @Test
    void listScenariosReturnsActiveScenariosSortedByRepository() {
        when(scenarioRepository.findByActiveTrueOrderByTitleAsc()).thenReturn(List.of(scenario("business-opening")));

        var scenarios = speakingService.listScenarios();

        assertThat(scenarios).extracting("id").containsExactly("business-opening");
    }

    @Test
    void createSessionUsesConfiguredOpeningMessageWithoutCallingAgent() {
        AppUser user = user(7L, "learner");
        SpeakingScenario scenario = scenario("business-opening");
        AtomicLong ids = new AtomicLong(100);
        List<SpeakingMessage> savedMessages = new ArrayList<>();
        when(userRepository.findByUsername("learner")).thenReturn(Optional.of(user));
        when(scenarioRepository.findById("business-opening")).thenReturn(Optional.of(scenario));
        when(sessionRepository.save(any(SpeakingSession.class))).thenAnswer(invocation -> {
            SpeakingSession session = invocation.getArgument(0);
            ReflectionTestUtils.setField(session, "id", 99L);
            return session;
        });
        when(messageRepository.save(any(SpeakingMessage.class))).thenAnswer(invocation -> {
            SpeakingMessage message = invocation.getArgument(0);
            if (message.getId() == null) {
                ReflectionTestUtils.setField(message, "id", ids.getAndIncrement());
                savedMessages.add(message);
            }
            return message;
        });
        when(messageRepository.findBySessionIdOrderByTurnIndexAscCreatedAtAsc(99L))
                .thenAnswer(invocation -> savedMessages);

        SpeakingSessionResponse response =
                speakingService.createSession("learner", new CreateSpeakingSessionRequest("business-opening", " Hometown "));

        assertThat(response.id()).isEqualTo(99L);
        assertThat(response.scenario().id()).isEqualTo("business-opening");
        assertThat(response.selectedTopic()).isEqualTo("Hometown");
        assertThat(response.messages()).hasSize(1);
        assertThat(response.messages().get(0).content()).isEqualTo("Hello");
        assertThat(response.messages().get(0).spokenText()).isEqualTo("Hello");
        assertThat(response.messages().get(0).autoPlay()).isTrue();
        assertThat(response.messages().get(0).audioUrl()).isNull();
        assertThat(response.messages().get(0).audioPending()).isTrue();
        verify(agentClient, never()).reply(eq(scenario), eq("Hometown"), any(), eq(""), eq(0));
        verify(agentAudioSynthesisService).synthesizeAgentMessageAsync(100L);
    }

    @Test
    void createSessionRejectsInactiveScenario() {
        SpeakingScenario scenario = scenario("business-opening");
        scenario.setActive(false);
        when(userRepository.findByUsername("learner")).thenReturn(Optional.of(user(7L, "learner")));
        when(scenarioRepository.findById("business-opening")).thenReturn(Optional.of(scenario));

        assertThatThrownBy(() -> speakingService.createSession(
                "learner",
                new CreateSpeakingSessionRequest("business-opening", null)
        ))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Speaking scenario was not found.");
    }

    @Test
    void getSessionRejectsSessionOwnedByAnotherUser() {
        SpeakingSession session = session(99L, user(8L, "other"), scenario("business-opening"));
        when(sessionRepository.findById(99L)).thenReturn(Optional.of(session));

        assertThatThrownBy(() -> speakingService.getSession("learner", 99L))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage("This speaking session belongs to another user.");
    }

    @Test
    void submitRecordingProcessesSpeechAndPersistsUserAndAgentMessages() throws Exception {
        AppUser user = user(7L, "learner");
        SpeakingScenario scenario = scenario("business-opening");
        SpeakingSession session = session(99L, user, scenario);
        session.setSelectedTopic("Work");
        AtomicLong ids = new AtomicLong(100);
        List<SpeakingMessage> savedMessages = new ArrayList<>();
        when(sessionRepository.findById(99L)).thenReturn(Optional.of(session));
        when(messageRepository.save(any(SpeakingMessage.class))).thenAnswer(invocation -> {
            SpeakingMessage message = invocation.getArgument(0);
            if (message.getId() == null) {
                ReflectionTestUtils.setField(message, "id", ids.getAndIncrement());
            }
            savedMessages.add(message);
            return message;
        });
        when(audioStorageService.save(eq(99L), any(), any())).thenReturn("/audio/turn.wav");
        when(asrService.transcribe(any(), any())).thenReturn("Hello, nice to meet you.");
        when(messageRepository.findBySessionIdOrderByTurnIndexAscCreatedAtAsc(99L)).thenReturn(List.of());
        when(agentClient.reply(eq(scenario), eq("Work"), any(), eq("Hello, nice to meet you."), eq(1)))
                .thenReturn(new SpeakingAgentReply("Welcome to the meeting.", "Welcome spoken.", "Use a fuller greeting."));
        when(sessionRepository.save(session)).thenReturn(session);

        SpeakingTurnResponse response = speakingService.submitRecording(
                "learner",
                99L,
                new MockMultipartFile("audio", "turn.wav", "audio/wav", new byte[] {1, 2, 3}),
                1234L
        );

        assertThat(response.userMessage().content()).isEqualTo("Hello, nice to meet you.");
        assertThat(response.userMessage().id()).isEqualTo(100L);
        assertThat(response.userMessage().spokenText()).isNull();
        assertThat(response.userMessage().autoPlay()).isFalse();
        assertThat(response.agentMessage().content()).isEqualTo("Welcome to the meeting.");
        assertThat(response.agentMessage().spokenText()).isEqualTo("Welcome spoken.");
        assertThat(response.agentMessage().autoPlay()).isTrue();
        assertThat(response.agentMessage().audioPending()).isTrue();
        assertThat(response.pronunciationScore()).isNull();
        assertThat(response.session().currentTurn()).isEqualTo(1);
        assertThat(savedMessages)
                .filteredOn(message -> Long.valueOf(100L).equals(message.getId()))
                .extracting(SpeakingMessage::getDurationMs)
                .contains(1234L);
        verify(iseService, never()).evaluate(any(), any());
        verify(asrService).transcribe(any(), eq("turn.wav"));
        verify(agentClient).reply(eq(scenario), eq("Work"), any(), eq("Hello, nice to meet you."), eq(1));
        verify(agentAudioSynthesisService).synthesizeAgentMessageAsync(101L);
    }

    @Test
    void submitRecordingRejectsCompletedSession() {
        SpeakingSession session = session(99L, user(7L, "learner"), scenario("business-opening"));
        session.setStatus(SpeakingSessionStatus.COMPLETED);
        when(sessionRepository.findById(99L)).thenReturn(Optional.of(session));

        assertThatThrownBy(() -> speakingService.submitRecording(
                "learner",
                99L,
                new MockMultipartFile("audio", "turn.wav", "audio/wav", new byte[] {1}),
                1000L
        ))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Speaking session is not active.");
    }

    @Test
    void submitRecordingSkipsPronunciationEvaluationForChineseHelpRequest() throws Exception {
        AppUser user = user(7L, "learner");
        SpeakingScenario scenario = scenario("business-opening");
        SpeakingSession session = session(99L, user, scenario);
        AtomicLong ids = new AtomicLong(100);
        when(sessionRepository.findById(99L)).thenReturn(Optional.of(session));
        when(messageRepository.save(any(SpeakingMessage.class))).thenAnswer(invocation -> {
            SpeakingMessage message = invocation.getArgument(0);
            if (message.getId() == null) {
                ReflectionTestUtils.setField(message, "id", ids.getAndIncrement());
            }
            return message;
        });
        when(audioStorageService.save(eq(99L), any(), any())).thenReturn("/audio/turn.webm");
        when(asrService.transcribe(any(), any())).thenReturn("这个问题我不会回答");
        when(messageRepository.findBySessionIdOrderByTurnIndexAscCreatedAtAsc(99L)).thenReturn(List.of());
        when(agentClient.reply(eq(scenario), any(), any(), eq("这个问题我不会回答"), eq(0)))
                .thenReturn(new SpeakingAgentReply(
                        "这是中文求助的解释。",
                        null,
                        "可以说：I am not sure how to answer this question。"
                ));
        when(sessionRepository.save(session)).thenReturn(session);

        speakingService = new SpeakingServiceImpl(
                scenarioRepository,
                sessionRepository,
                messageRepository,
                userRepository,
                agentClient,
                asrService,
                iseService,
                audioStorageService,
                agentAudioSynthesisService,
                pronunciationEvaluationService,
                objectMapper,
                true,
                true
        );

        SpeakingTurnResponse response = speakingService.submitRecording(
                "learner",
                99L,
                new MockMultipartFile("audio", "turn.webm", "audio/webm", new byte[] {1, 2, 3}),
                1200L
        );

        assertThat(response.userMessage().pronunciationScore()).isNull();
        assertThat(response.userMessage().turnIndex()).isZero();
        assertThat(response.agentMessage().turnIndex()).isZero();
        assertThat(response.agentMessage().content()).isEqualTo("这是中文求助的解释。");
        assertThat(response.agentMessage().spokenText()).isNull();
        assertThat(response.agentMessage().audioPending()).isFalse();
        assertThat(response.session().currentTurn()).isZero();
        assertThat(session.getCurrentTurn()).isZero();
        verify(iseService, never()).evaluate(any(), any());
        verify(pronunciationEvaluationService, never()).evaluateUserMessageAsync(any(), any(), any());
        verify(agentClient).reply(eq(scenario), any(), any(), eq("这个问题我不会回答"), eq(0));
        verify(agentAudioSynthesisService, never()).synthesizeAgentMessageAsync(any());
    }

    @Test
    void submitRecordingEvaluatesPronunciationSynchronouslyForEligibleEnglishSpeech() throws Exception {
        AppUser user = user(7L, "learner");
        SpeakingScenario scenario = scenario("business-opening");
        SpeakingSession session = session(99L, user, scenario);
        AtomicLong ids = new AtomicLong(100);
        PronunciationScore score = new PronunciationScore(88, 90, 86, 95, 0);
        when(sessionRepository.findById(99L)).thenReturn(Optional.of(session));
        when(messageRepository.save(any(SpeakingMessage.class))).thenAnswer(invocation -> {
            SpeakingMessage message = invocation.getArgument(0);
            if (message.getId() == null) {
                ReflectionTestUtils.setField(message, "id", ids.getAndIncrement());
            }
            return message;
        });
        when(audioStorageService.save(eq(99L), any(), any())).thenReturn("/audio/turn.webm");
        when(asrService.transcribe(any(), any())).thenReturn("This is my answer.");
        when(iseService.evaluate(any(), eq("This is my answer."))).thenReturn(score);
        when(objectMapper.writeValueAsString(score)).thenReturn("{\"totalScore\":88}");
        when(messageRepository.findBySessionIdOrderByTurnIndexAscCreatedAtAsc(99L)).thenReturn(List.of());
        when(agentClient.reply(eq(scenario), any(), any(), eq("This is my answer."), eq(1)))
                .thenReturn(new SpeakingAgentReply("Good answer.", " ", null));
        when(sessionRepository.save(session)).thenReturn(session);

        speakingService = new SpeakingServiceImpl(
                scenarioRepository,
                sessionRepository,
                messageRepository,
                userRepository,
                agentClient,
                asrService,
                iseService,
                audioStorageService,
                agentAudioSynthesisService,
                pronunciationEvaluationService,
                objectMapper,
                true,
                true
        );

        SpeakingTurnResponse response = speakingService.submitRecording(
                "learner",
                99L,
                new MockMultipartFile("audio", "turn.webm", "audio/webm", new byte[] {1, 2, 3}),
                null
        );

        assertThat(response.pronunciationScore()).isEqualTo(score);
        assertThat(response.userMessage().pronunciationScore()).isEqualTo(88.0);
        assertThat(response.agentMessage().spokenText()).isEqualTo("Good answer.");
        verify(pronunciationEvaluationService, never()).evaluateUserMessageAsync(any(), any(), any());
    }

    @Test
    void submitRecordingSchedulesAsyncPronunciationWhenTurnScoreIsMissing() throws Exception {
        AppUser user = user(7L, "learner");
        SpeakingScenario scenario = scenario("business-opening");
        SpeakingSession session = session(99L, user, scenario);
        AtomicLong ids = new AtomicLong(100);
        when(sessionRepository.findById(99L)).thenReturn(Optional.of(session));
        when(messageRepository.save(any(SpeakingMessage.class))).thenAnswer(invocation -> {
            SpeakingMessage message = invocation.getArgument(0);
            if (message.getId() == null) {
                ReflectionTestUtils.setField(message, "id", ids.getAndIncrement());
            }
            return message;
        });
        when(audioStorageService.save(eq(99L), any(), any())).thenReturn("/audio/turn.webm");
        when(asrService.transcribe(any(), any())).thenReturn("This is an eligible answer.");
        when(messageRepository.findBySessionIdOrderByTurnIndexAscCreatedAtAsc(99L)).thenReturn(List.of());
        when(agentClient.reply(eq(scenario), any(), any(), eq("This is an eligible answer."), eq(1)))
                .thenReturn(new SpeakingAgentReply("Thanks.", null, null));
        when(sessionRepository.save(session)).thenReturn(session);

        speakingService = new SpeakingServiceImpl(
                scenarioRepository,
                sessionRepository,
                messageRepository,
                userRepository,
                agentClient,
                asrService,
                iseService,
                audioStorageService,
                agentAudioSynthesisService,
                pronunciationEvaluationService,
                objectMapper,
                false,
                true
        );

        SpeakingTurnResponse response = speakingService.submitRecording(
                "learner",
                99L,
                new MockMultipartFile("audio", "turn.webm", "audio/webm", new byte[] {1, 2, 3}),
                0L
        );

        assertThat(response.userMessage().pronunciationScore()).isNull();
        verify(pronunciationEvaluationService)
                .evaluateUserMessageAsync(eq(100L), any(), eq("This is an eligible answer."));
    }

    @Test
    void getFeedbackUsesAverageStoredPronunciationScoresForSummary() throws Exception {
        AppUser user = user(7L, "learner");
        SpeakingScenario scenario = scenario("business-opening");
        SpeakingSession session = session(99L, user, scenario);
        session.setCurrentTurn(2);
        SpeakingMessage opening = agentMessage(session, 1L, 0, "Hello", null);
        SpeakingMessage userTurn1 = userMessage(
                session,
                2L,
                1,
                "I would like to discuss the agenda.",
                new PronunciationScore(80, 70, 60, 90, 110)
        );
        SpeakingMessage agentTurn1 = agentMessage(session, 3L, 1, "Could you explain the first item?", null);
        SpeakingMessage userTurn2 = userMessage(
                session,
                4L,
                2,
                "The first item is our timeline.",
                new PronunciationScore(90, 80, 70, 100, 130)
        );
        SpeakingMessage agentTurn2 = agentMessage(session, 5L, 2, "What is the main risk?", null);
        userTurn1.setDurationMs(3000L);
        userTurn2.setDurationMs(3500L);
        when(sessionRepository.findById(99L)).thenReturn(Optional.of(session));
        when(messageRepository.findBySessionIdOrderByTurnIndexAscCreatedAtAsc(99L))
                .thenReturn(List.of(opening, userTurn1, agentTurn1, userTurn2, agentTurn2));
        when(objectMapper.readValue(userTurn1.getPronunciationDetail(), PronunciationScore.class))
                .thenReturn(new PronunciationScore(80, 70, 60, 90, 110));
        when(objectMapper.readValue(userTurn2.getPronunciationDetail(), PronunciationScore.class))
                .thenReturn(new PronunciationScore(90, 80, 70, 100, 130));

        SpeakingFeedbackResponse feedback = speakingService.getFeedback("learner", 99L);

        assertThat(feedback.totalScore()).isEqualTo(72);
        assertThat(feedback.pronunciation()).isEqualTo(75);
        assertThat(feedback.fluency()).isEqualTo(65);
        assertThat(feedback.integrity()).isEqualTo(95);
        assertThat(feedback.speed()).isEqualTo("120 WPM");
        assertThat(feedback.issueSentences()).isEmpty();
        assertThat(feedback.averagePronunciationScore()).isEqualTo(71.5);
        assertThat(feedback.turns()).hasSize(2);
        assertThat(feedback.turns().get(0).score().totalScore()).isEqualTo(66.5);
        assertThat(feedback.turns().get(1).score().totalScore()).isEqualTo(76.5);
    }

    @Test
    void getFeedbackUsesLowTotalScoreUserMessagesAsIssueSentences() throws Exception {
        AppUser user = user(7L, "learner");
        SpeakingScenario scenario = scenario("business-opening");
        SpeakingSession session = session(99L, user, scenario);
        session.setCurrentTurn(2);
        SpeakingMessage lowScoreTurn = userMessage(
                session,
                2L,
                1,
                "I need book room.",
                new PronunciationScore(55, 52, 58, 70, 90)
        );
        SpeakingMessage passingTurn = userMessage(
                session,
                4L,
                2,
                "I would like to book a room.",
                new PronunciationScore(60, 62, 61, 75, 100)
        );
        when(sessionRepository.findById(99L)).thenReturn(Optional.of(session));
        when(messageRepository.findBySessionIdOrderByTurnIndexAscCreatedAtAsc(99L))
                .thenReturn(List.of(
                        agentMessage(session, 1L, 0, "Hello", null),
                        lowScoreTurn,
                        agentMessage(session, 3L, 1, "Could you say that again?", null),
                        passingTurn,
                        agentMessage(session, 5L, 2, "Sure.", null)
                ));
        when(objectMapper.readValue(lowScoreTurn.getPronunciationDetail(), PronunciationScore.class))
                .thenReturn(new PronunciationScore(55, 52, 58, 70, 90));
        when(objectMapper.readValue(passingTurn.getPronunciationDetail(), PronunciationScore.class))
                .thenReturn(new PronunciationScore(60, 62, 61, 75, 100));

        SpeakingFeedbackResponse feedback = speakingService.getFeedback("learner", 99L);

        assertThat(feedback.issueSentences()).containsExactly("I need book room.");
    }

    @Test
    void getFeedbackEvaluatesMissingPronunciationAndHandlesEmptyTurns() {
        AppUser user = user(7L, "learner");
        SpeakingScenario scenario = scenario("business-opening");
        SpeakingSession session = session(99L, user, scenario);
        session.setCurrentTurn(2);
        SpeakingMessage userTurn1 = message(session, 2L, 1, SpeakingMessageSender.USER, " ");
        userTurn1.setAudioUrl("/audio/turn.webm");
        userTurn1.setTranscribedText("This answer needs review.");
        userTurn1.setDurationMs(2_000L);
        when(sessionRepository.findById(99L)).thenReturn(Optional.of(session));
        when(messageRepository.findBySessionIdOrderByTurnIndexAscCreatedAtAsc(99L))
                .thenReturn(List.of(userTurn1));
        when(audioStorageService.load("/audio/turn.webm")).thenReturn(new byte[] {1, 2, 3});
        when(pronunciationEvaluationService.evaluateUserMessage(
                eq(2L),
                any(),
                eq("This answer needs review.")
        )).thenReturn(Optional.of(new PronunciationScore(50, 55, 45, 70, 0)));

        SpeakingFeedbackResponse feedback = speakingService.getFeedback("learner", 99L);

        assertThat(feedback.turns()).hasSize(2);
        assertThat(feedback.turns().get(0).score()).isNotNull();
        assertThat(feedback.turns().get(0).agentText()).isEmpty();
        assertThat(feedback.turns().get(1).userText()).isEmpty();
        assertThat(feedback.issueSentences()).isEmpty();
    }

    @Test
    void getFeedbackFallsBackToStoredTotalWhenPronunciationDetailIsBlank() {
        AppUser user = user(7L, "learner");
        SpeakingScenario scenario = scenario("business-opening");
        SpeakingSession session = session(99L, user, scenario);
        session.setCurrentTurn(1);
        SpeakingMessage userTurn = message(session, 2L, 1, SpeakingMessageSender.USER, "A short answer.");
        userTurn.setPronunciationScore(72.0);
        userTurn.setPronunciationDetail(" ");
        when(sessionRepository.findById(99L)).thenReturn(Optional.of(session));
        when(messageRepository.findBySessionIdOrderByTurnIndexAscCreatedAtAsc(99L))
                .thenReturn(List.of(userTurn));

        SpeakingFeedbackResponse feedback = speakingService.getFeedback("learner", 99L);

        assertThat(feedback.totalScore()).isEqualTo(72);
        assertThat(feedback.pronunciation()).isEqualTo(72);
    }

    @Test
    void evaluateMissingPronunciationSkipsInvalidOrUnsupportedMessages() {
        SpeakingMessage noAudio = message(null, 1L, 1, SpeakingMessageSender.USER, "I feel sick.");
        noAudio.setTranscribedText("I feel sick.");

        SpeakingMessage blankAudio = message(null, 2L, 1, SpeakingMessageSender.USER, "I feel sick.");
        blankAudio.setAudioUrl(" ");
        blankAudio.setTranscribedText("I feel sick.");

        SpeakingMessage noTranscript = message(null, 3L, 1, SpeakingMessageSender.USER, "");
        noTranscript.setAudioUrl("/audio/one.webm");

        SpeakingMessage blankTranscript = message(null, 4L, 1, SpeakingMessageSender.USER, "");
        blankTranscript.setAudioUrl("/audio/two.webm");
        blankTranscript.setTranscribedText(" ");

        SpeakingMessage chineseTranscript = message(null, 5L, 1, SpeakingMessageSender.USER, "中文求助");
        chineseTranscript.setAudioUrl("/audio/three.webm");
        chineseTranscript.setTranscribedText("我想要中文帮助");

        assertThat((PronunciationScore) ReflectionTestUtils.invokeMethod(
                speakingService, "evaluateMissingPronunciation", noAudio)).isNull();
        assertThat((PronunciationScore) ReflectionTestUtils.invokeMethod(
                speakingService, "evaluateMissingPronunciation", blankAudio)).isNull();
        assertThat((PronunciationScore) ReflectionTestUtils.invokeMethod(
                speakingService, "evaluateMissingPronunciation", noTranscript)).isNull();
        assertThat((PronunciationScore) ReflectionTestUtils.invokeMethod(
                speakingService, "evaluateMissingPronunciation", blankTranscript)).isNull();
        assertThat((PronunciationScore) ReflectionTestUtils.invokeMethod(
                speakingService, "evaluateMissingPronunciation", chineseTranscript)).isNull();
        verify(audioStorageService, never()).load(any());
    }

    @Test
    void evaluateMissingPronunciationReturnsNullWhenEvaluatorHasNoScore() {
        SpeakingMessage message = message(null, 6L, 1, SpeakingMessageSender.USER, "A valid English answer.");
        message.setAudioUrl("/audio/four.webm");
        message.setTranscribedText("A valid English answer.");
        when(audioStorageService.load("/audio/four.webm")).thenReturn(new byte[] {4, 5, 6});
        when(pronunciationEvaluationService.evaluateUserMessage(eq(6L), any(), eq("A valid English answer.")))
                .thenReturn(Optional.empty());

        assertThat((PronunciationScore) ReflectionTestUtils.invokeMethod(
                speakingService, "evaluateMissingPronunciation", message)).isNull();
    }

    private static AppUser user(Long id, String username) {
        AppUser user = new AppUser();
        ReflectionTestUtils.setField(user, "id", id);
        user.setUsername(username);
        user.setEmail(username + "@example.com");
        user.setDisplayName("Learner");
        user.setPasswordHash("hashed-password");
        user.setRole(UserRole.USER);
        user.setEnabled(true);
        return user;
    }

    private static SpeakingScenario scenario(String id) {
        SpeakingScenario scenario = new SpeakingScenario();
        scenario.setId(id);
        scenario.setTitle("Business Opening");
        scenario.setDescription("description");
        scenario.setDifficulty("starter");
        scenario.setAccent("US");
        scenario.setDuration("5 min");
        scenario.setSummary("summary");
        scenario.setTone("friendly");
        scenario.setGoal("goal");
        scenario.setKeywords("greeting,meeting");
        scenario.setRolePrompt("role prompt");
        scenario.setOpeningMessage("Hello");
        scenario.setTargetTurns(3);
        scenario.setScoringRubric("rubric");
        scenario.setActive(true);
        return scenario;
    }

    private static SpeakingSession session(Long id, AppUser user, SpeakingScenario scenario) {
        SpeakingSession session = new SpeakingSession();
        ReflectionTestUtils.setField(session, "id", id);
        session.setUser(user);
        session.setScenario(scenario);
        session.setStatus(SpeakingSessionStatus.ACTIVE);
        session.setStartedAt(Instant.parse("2026-07-21T00:00:00Z"));
        session.setTargetTurns(3);
        session.setCurrentTurn(0);
        return session;
    }

    private static SpeakingMessage userMessage(
            SpeakingSession session,
            Long id,
            int turnIndex,
            String content,
            PronunciationScore score
    ) {
        SpeakingMessage message = message(session, id, turnIndex, SpeakingMessageSender.USER, content);
        message.setPronunciationScore(score.totalScore());
        message.setPronunciationDetail("""
                {"totalScore":%s,"accuracy":%s,"fluency":%s,"integrity":%s,"speed":%s}
                """.formatted(score.totalScore(), score.accuracy(), score.fluency(), score.integrity(), score.speed()));
        return message;
    }

    private static SpeakingMessage agentMessage(
            SpeakingSession session,
            Long id,
            int turnIndex,
            String content,
            String instantTip
    ) {
        SpeakingMessage message = message(session, id, turnIndex, SpeakingMessageSender.AGENT, content);
        message.setInstantTip(instantTip);
        return message;
    }

    private static SpeakingMessage message(
            SpeakingSession session,
            Long id,
            int turnIndex,
            SpeakingMessageSender sender,
            String content
    ) {
        SpeakingMessage message = new SpeakingMessage();
        ReflectionTestUtils.setField(message, "id", id);
        ReflectionTestUtils.setField(message, "createdAt", Instant.parse("2026-07-21T00:00:00Z"));
        message.setSession(session);
        message.setTurnIndex(turnIndex);
        message.setSender(sender);
        message.setContent(content);
        return message;
    }
}
