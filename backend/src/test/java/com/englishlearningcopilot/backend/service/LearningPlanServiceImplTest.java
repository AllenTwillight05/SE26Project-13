package com.englishlearningcopilot.backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.englishlearningcopilot.backend.dto.LearningPlanRequest;
import com.englishlearningcopilot.backend.dto.ProfileSnapshotResponse;
import com.englishlearningcopilot.backend.entity.AppUser;
import com.englishlearningcopilot.backend.entity.SpeakingMessage;
import com.englishlearningcopilot.backend.entity.SpeakingMessageSender;
import com.englishlearningcopilot.backend.entity.SpeakingScenario;
import com.englishlearningcopilot.backend.entity.SpeakingSession;
import com.englishlearningcopilot.backend.entity.UserDailyLearningProgress;
import com.englishlearningcopilot.backend.entity.UserLearningPlan;
import com.englishlearningcopilot.backend.entity.UserRole;
import com.englishlearningcopilot.backend.entity.UserWordProgress;
import com.englishlearningcopilot.backend.exception.ResourceNotFoundException;
import com.englishlearningcopilot.backend.repository.SpeakingMessageRepository;
import com.englishlearningcopilot.backend.repository.SpeakingSessionRepository;
import com.englishlearningcopilot.backend.repository.UserDailyLearningProgressRepository;
import com.englishlearningcopilot.backend.repository.UserDailyPracticeLogRepository;
import com.englishlearningcopilot.backend.repository.UserLearningPlanRepository;
import com.englishlearningcopilot.backend.repository.UserRepository;
import com.englishlearningcopilot.backend.repository.UserWordProgressRepository;
import com.englishlearningcopilot.backend.service.impl.LearningPlanServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class LearningPlanServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserLearningPlanRepository userLearningPlanRepository;

    @Mock
    private UserDailyLearningProgressRepository userDailyLearningProgressRepository;

    @Mock
    private UserDailyPracticeLogRepository userDailyPracticeLogRepository;

    @Mock
    private SpeakingSessionRepository speakingSessionRepository;

    @Mock
    private SpeakingMessageRepository speakingMessageRepository;

    @Mock
    private UserWordProgressRepository userWordProgressRepository;

    private LearningPlanServiceImpl learningPlanService;

    @BeforeEach
    void setUp() {
        learningPlanService = new LearningPlanServiceImpl(
                userRepository,
                userLearningPlanRepository,
                userDailyLearningProgressRepository,
                userDailyPracticeLogRepository,
                speakingSessionRepository,
                speakingMessageRepository,
                userWordProgressRepository,
                new ObjectMapper()
        );
    }

    @Test
    void getLearningPlanCreatesDefaultPlanWhenNeeded() {
        AppUser user = user(7L, "learner", "");
        UserLearningPlan plan = plan(7L, 20, 12);
        when(userRepository.findByUsername("learner")).thenReturn(Optional.of(user));
        when(userLearningPlanRepository.findByUserId(7L))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(plan));

        var response = learningPlanService.getLearningPlan("learner");

        assertThat(response.dailyVocabularyGoal()).isEqualTo(20);
        assertThat(response.dailyGrammarGoal()).isEqualTo(12);
        assertThat(response.enabled()).isTrue();
        verify(userLearningPlanRepository).insertDefaultIfAbsent(7L, 20, 12);
    }

    @Test
    void updateLearningPlanRefreshesTodayProgressAndCompletion() {
        AppUser user = user(7L, "learner", "Learner");
        UserLearningPlan plan = plan(7L, 20, 12);
        UserDailyLearningProgress progress = progress(7L, 5, 3, 5, 3, false);
        when(userRepository.findByUsername("learner")).thenReturn(Optional.of(user));
        when(userLearningPlanRepository.findByUserId(7L)).thenReturn(Optional.of(plan));
        when(userLearningPlanRepository.save(plan)).thenReturn(plan);
        when(userDailyLearningProgressRepository.findByUserIdAndPlanDate(eq(7L), any(LocalDate.class)))
                .thenReturn(Optional.of(progress));

        var response = learningPlanService.updateLearningPlan("learner", new LearningPlanRequest(5, 3));

        assertThat(response.dailyVocabularyGoal()).isEqualTo(5);
        assertThat(response.dailyGrammarGoal()).isEqualTo(3);
        assertThat(progress.isCompleted()).isTrue();
        assertThat(progress.getCompletedAt()).isNotNull();
        verify(userDailyLearningProgressRepository).save(progress);
    }

    @Test
    void getDailyStatusClearsCompletedAtWhenProgressNoLongerDone() {
        AppUser user = user(7L, "learner", "Learner");
        UserLearningPlan plan = plan(7L, 4, 4);
        UserDailyLearningProgress progress = progress(7L, 4, 1, 4, 4, true);
        progress.setCompletedAt(Instant.now());
        when(userRepository.findByUsername("learner")).thenReturn(Optional.of(user));
        when(userLearningPlanRepository.findByUserId(7L)).thenReturn(Optional.of(plan));
        when(userDailyLearningProgressRepository.findByUserIdAndPlanDate(eq(7L), any(LocalDate.class)))
                .thenReturn(Optional.of(progress));
        when(userDailyLearningProgressRepository.findByUserIdAndCompletedTrueOrderByPlanDateDesc(7L))
                .thenReturn(List.of());

        var status = learningPlanService.getDailyStatus("learner");

        assertThat(status.vocabulary().done()).isTrue();
        assertThat(status.grammar().done()).isFalse();
        assertThat(status.allDone()).isFalse();
        assertThat(progress.getCompletedAt()).isNull();
    }

    @Test
    void recordVocabularyPracticeIgnoresDuplicateDailyItem() {
        when(userDailyPracticeLogRepository.insertIfAbsent(eq(7L), any(LocalDate.class), eq("VOCABULARY"), eq("10")))
                .thenReturn(0);

        learningPlanService.recordVocabularyPractice(7L, 10L);

        verify(userLearningPlanRepository, never()).findByUserId(any());
        verify(userDailyLearningProgressRepository, never()).incrementVocabularyCompletion(any(), any());
    }

    @Test
    void recordGrammarPracticeCreatesProgressAndIncrementsGrammarCompletion() {
        UserLearningPlan plan = plan(7L, 20, 12);
        UserDailyLearningProgress progress = progress(7L, 0, 0, 20, 12, false);
        when(userDailyPracticeLogRepository.insertIfAbsent(eq(7L), any(LocalDate.class), eq("GRAMMAR"), eq("3")))
                .thenReturn(1);
        when(userLearningPlanRepository.findByUserId(7L)).thenReturn(Optional.of(plan));
        when(userDailyLearningProgressRepository.findByUserIdAndPlanDate(eq(7L), any(LocalDate.class)))
                .thenReturn(Optional.of(progress));

        learningPlanService.recordGrammarPractice(7L, 3);

        verify(userDailyLearningProgressRepository).incrementGrammarCompletion(eq(7L), any(LocalDate.class));
    }

    @Test
    void profileSnapshotUsesWeeklySpeakingAverageAndFsrsRetentionRates() {
        AppUser user = user(7L, "learner", "Learner");
        UserLearningPlan plan = plan(7L, 20, 12);
        UserDailyLearningProgress todayProgress = progress(7L, 2, 1, 20, 12, false);
        when(userRepository.findByUsername("learner")).thenReturn(Optional.of(user));
        when(userLearningPlanRepository.findByUserId(7L)).thenReturn(Optional.of(plan));
        when(userDailyLearningProgressRepository.findByUserIdAndPlanDate(eq(7L), any(LocalDate.class)))
                .thenReturn(Optional.of(todayProgress));
        when(userDailyLearningProgressRepository.findByUserIdAndCompletedTrueOrderByPlanDateDesc(7L))
                .thenReturn(List.of());
        when(speakingSessionRepository.findByUserUsernameOrderByStartedAtDesc("learner")).thenReturn(List.of());
        when(speakingMessageRepository.findBySessionUserIdAndSenderAndCreatedAtBetween(
                eq(7L),
                eq(SpeakingMessageSender.USER),
                any(Instant.class),
                any(Instant.class)
        )).thenReturn(List.of(scoredMessage(90, 80)));
        when(userWordProgressRepository.findByUserIdAndQuestionType(7L, "vocabulary"))
                .thenReturn(List.of(reviewCard()));
        when(userWordProgressRepository.findByUserIdAndQuestionType(7L, "grammar"))
                .thenReturn(List.of(reviewCard()));

        ProfileSnapshotResponse snapshot = learningPlanService.getProfileSnapshot("learner");

        assertThat(snapshot.dailyPlan().progress()).extracting(ProfileSnapshotResponse.ProgressMetric::id)
                .containsExactly("fluency", "vocabulary-retention", "grammar-retention");
        assertThat(snapshot.dailyPlan().progress()).extracting(ProfileSnapshotResponse.ProgressMetric::value)
                .containsExactly(87, 100, 100);
        verify(userLearningPlanRepository, never()).insertDefaultIfAbsent(anyLong(), anyInt(), anyInt());
    }

    @Test
    void getProfileSnapshotUsesLatestSpeakingFeedbackAndFallbackDisplayName() {
        AppUser user = user(7L, "learner", " ");
        UserLearningPlan plan = plan(7L, 1, 1);
        UserDailyLearningProgress progress = progress(7L, 1, 1, 1, 1, true);
        SpeakingSession session = speakingSession(99L, "Airport", Instant.parse("2026-01-01T10:00:00Z"));
        SpeakingMessage weakMessage = userMessage("I am go airport.", 55, """
                {"totalScore":55,"accuracy":60,"fluency":50,"integrity":70,"speed":0}
                """);
        SpeakingMessage strongMessage = userMessage("Hello.", 85, """
                {"totalScore":85,"accuracy":90,"fluency":80,"integrity":85,"speed":0}
                """);
        when(userRepository.findByUsername("learner")).thenReturn(Optional.of(user));
        when(userLearningPlanRepository.findByUserId(7L)).thenReturn(Optional.of(plan));
        when(userDailyLearningProgressRepository.findByUserIdAndPlanDate(eq(7L), any(LocalDate.class)))
                .thenReturn(Optional.of(progress));
        when(userDailyLearningProgressRepository.findByUserIdAndCompletedTrueOrderByPlanDateDesc(7L))
                .thenReturn(List.of(progress));
        when(speakingMessageRepository.findBySessionUserIdAndSenderAndCreatedAtBetween(
                eq(7L),
                eq(SpeakingMessageSender.USER),
                any(Instant.class),
                any(Instant.class)
        )).thenReturn(List.of());
        when(userWordProgressRepository.findByUserIdAndQuestionType(eq(7L), any())).thenReturn(List.of());
        when(speakingSessionRepository.findByUserUsernameOrderByStartedAtDesc("learner"))
                .thenReturn(List.of(session));
        when(speakingMessageRepository.findBySessionIdOrderByTurnIndexAscCreatedAtAsc(99L))
                .thenReturn(List.of(weakMessage, strongMessage));

        ProfileSnapshotResponse snapshot = learningPlanService.getProfileSnapshot("learner");

        assertThat(snapshot.learnerName()).isEqualTo("learner");
        assertThat(snapshot.feedback().scenarioTitle()).isEqualTo("Airport");
        assertThat(snapshot.feedback().totalScore()).isEqualTo(72);
        assertThat(snapshot.feedback().pronunciation()).isEqualTo(75);
        assertThat(snapshot.feedback().fluency()).isEqualTo(65);
        assertThat(snapshot.feedback().integrity()).isEqualTo(78);
        assertThat(snapshot.feedback().issueSentence()).isEqualTo("I am go airport.");
    }

    @Test
    void getProfileSnapshotFallsBackWhenNoSpeakingFeedbackExists() {
        AppUser user = user(7L, "learner", "Learner");
        UserLearningPlan plan = plan(7L, 0, 0);
        UserDailyLearningProgress progress = progress(7L, 0, 0, 0, 0, true);
        when(userRepository.findByUsername("learner")).thenReturn(Optional.of(user));
        when(userLearningPlanRepository.findByUserId(7L)).thenReturn(Optional.of(plan));
        when(userDailyLearningProgressRepository.findByUserIdAndPlanDate(eq(7L), any(LocalDate.class)))
                .thenReturn(Optional.of(progress));
        when(userDailyLearningProgressRepository.findByUserIdAndCompletedTrueOrderByPlanDateDesc(7L))
                .thenReturn(List.of(progress));
        when(speakingMessageRepository.findBySessionUserIdAndSenderAndCreatedAtBetween(
                eq(7L),
                eq(SpeakingMessageSender.USER),
                any(Instant.class),
                any(Instant.class)
        )).thenReturn(List.of());
        when(userWordProgressRepository.findByUserIdAndQuestionType(eq(7L), any())).thenReturn(List.of());
        when(speakingSessionRepository.findByUserUsernameOrderByStartedAtDesc("learner")).thenReturn(List.of());

        ProfileSnapshotResponse snapshot = learningPlanService.getProfileSnapshot("learner");

        assertThat(snapshot.feedback().totalScore()).isNull();
        assertThat(snapshot.dailyPlan().vocabulary().done()).isTrue();
        assertThat(snapshot.dailyPlan().grammar().done()).isTrue();
    }

    @Test
    void getDailyStatusRequiresAuthentication() {
        assertThatThrownBy(() -> learningPlanService.getDailyStatus(null))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessage("Authentication is required.");
    }

    @Test
    void getLearningPlanRejectsMissingUser() {
        when(userRepository.findByUsername("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> learningPlanService.getLearningPlan("missing"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Current user was not found.");
    }

    private static AppUser user(Long id, String username, String displayName) {
        AppUser user = new AppUser();
        ReflectionTestUtils.setField(user, "id", id);
        user.setUsername(username);
        user.setEmail(username + "@example.com");
        user.setDisplayName(displayName);
        user.setPasswordHash("hashed-password");
        user.setRole(UserRole.USER);
        user.setEnabled(true);
        return user;
    }

    private static UserLearningPlan plan(Long userId, int vocabularyGoal, int grammarGoal) {
        UserLearningPlan plan = new UserLearningPlan();
        plan.setUserId(userId);
        plan.setDailyVocabularyGoal(vocabularyGoal);
        plan.setDailyGrammarGoal(grammarGoal);
        plan.setEnabled(true);
        return plan;
    }

    private static UserDailyLearningProgress progress(
            Long userId,
            int vocabularyCompleted,
            int grammarCompleted,
            int vocabularyGoal,
            int grammarGoal,
            boolean completed
    ) {
        UserDailyLearningProgress progress = new UserDailyLearningProgress();
        progress.setUserId(userId);
        progress.setPlanDate(LocalDate.now());
        progress.setVocabularyCompleted(vocabularyCompleted);
        progress.setGrammarCompleted(grammarCompleted);
        progress.setVocabularyGoal(vocabularyGoal);
        progress.setGrammarGoal(grammarGoal);
        progress.setCompleted(completed);
        return progress;
    }

    private static SpeakingSession speakingSession(Long id, String title, Instant completedAt) {
        SpeakingScenario scenario = new SpeakingScenario();
        scenario.setTitle(title);
        SpeakingSession session = new SpeakingSession();
        ReflectionTestUtils.setField(session, "id", id);
        session.setScenario(scenario);
        session.setStartedAt(completedAt.minusSeconds(600));
        session.setCompletedAt(completedAt);
        return session;
    }

    private static SpeakingMessage userMessage(String content, double storedScore, String detail) {
        SpeakingMessage message = new SpeakingMessage();
        message.setSender(SpeakingMessageSender.USER);
        message.setContent(content);
        message.setPronunciationScore(storedScore);
        message.setPronunciationDetail(detail);
        return message;
    }

    private static SpeakingMessage scoredMessage(double accuracy, double fluency) {
        SpeakingMessage message = new SpeakingMessage();
        message.setSender(SpeakingMessageSender.USER);
        message.setPronunciationScore(accuracy);
        message.setPronunciationDetail("""
                {"totalScore":%s,"accuracy":%s,"fluency":%s,"integrity":100,"speed":0}
                """.formatted(accuracy, accuracy, fluency));
        return message;
    }

    private static UserWordProgress reviewCard() {
        UserWordProgress progress = new UserWordProgress();
        progress.setState(1);
        progress.setStability(2.5);
        progress.setLastReview(Instant.now());
        return progress;
    }
}
