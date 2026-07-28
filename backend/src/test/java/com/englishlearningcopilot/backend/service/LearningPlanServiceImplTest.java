package com.englishlearningcopilot.backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.englishlearningcopilot.backend.dto.ProfileSnapshotResponse;
import com.englishlearningcopilot.backend.entity.AppUser;
import com.englishlearningcopilot.backend.entity.SpeakingMessage;
import com.englishlearningcopilot.backend.entity.SpeakingMessageSender;
import com.englishlearningcopilot.backend.entity.UserDailyLearningProgress;
import com.englishlearningcopilot.backend.entity.UserLearningPlan;
import com.englishlearningcopilot.backend.entity.UserWordProgress;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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

    @Test
    void profileSnapshotUsesWeeklySpeakingAverageAndFsrsRetentionRates() {
        LearningPlanServiceImpl service = new LearningPlanServiceImpl(
                userRepository,
                userLearningPlanRepository,
                userDailyLearningProgressRepository,
                userDailyPracticeLogRepository,
                speakingSessionRepository,
                speakingMessageRepository,
                userWordProgressRepository,
                new ObjectMapper()
        );
        AppUser user = user(7L, "learner");
        UserLearningPlan plan = plan();
        UserDailyLearningProgress todayProgress = todayProgress();

        when(userRepository.findByUsername("learner")).thenReturn(Optional.of(user));
        when(userLearningPlanRepository.findByUserId(7L)).thenReturn(Optional.of(plan));
        when(userDailyLearningProgressRepository.findByUserIdAndPlanDate(7L, LocalDate.now()))
                .thenReturn(Optional.of(todayProgress));
        when(userDailyLearningProgressRepository.findByUserIdAndCompletedTrueOrderByPlanDateDesc(7L))
                .thenReturn(List.of());
        when(speakingSessionRepository.findByUserUsernameOrderByStartedAtDesc("learner")).thenReturn(List.of());
        when(speakingMessageRepository.findBySessionUserIdAndSenderAndCreatedAtBetween(
                org.mockito.ArgumentMatchers.eq(7L),
                org.mockito.ArgumentMatchers.eq(SpeakingMessageSender.USER),
                org.mockito.ArgumentMatchers.any(Instant.class),
                org.mockito.ArgumentMatchers.any(Instant.class)
        )).thenReturn(List.of(scoredMessage(90, 80)));
        when(userWordProgressRepository.findByUserIdAndQuestionType(7L, "vocabulary"))
                .thenReturn(List.of(reviewCard()));
        when(userWordProgressRepository.findByUserIdAndQuestionType(7L, "grammar"))
                .thenReturn(List.of(reviewCard()));

        ProfileSnapshotResponse snapshot = service.getProfileSnapshot("learner");

        assertThat(snapshot.dailyPlan().progress()).containsExactly(
                new ProfileSnapshotResponse.ProgressMetric("fluency", "口语流利度", 87, "default"),
                new ProfileSnapshotResponse.ProgressMetric("vocabulary-retention", "词汇留存率", 100, "teal"),
                new ProfileSnapshotResponse.ProgressMetric("grammar-retention", "语法留存率", 100, "gold")
        );
    }

    private static AppUser user(Long id, String username) {
        AppUser user = new AppUser();
        ReflectionTestUtils.setField(user, "id", id);
        user.setUsername(username);
        user.setDisplayName("Learner");
        return user;
    }

    private static UserLearningPlan plan() {
        UserLearningPlan plan = new UserLearningPlan();
        plan.setDailyVocabularyGoal(20);
        plan.setDailyGrammarGoal(12);
        return plan;
    }

    private static UserDailyLearningProgress todayProgress() {
        UserDailyLearningProgress progress = new UserDailyLearningProgress();
        progress.setVocabularyCompleted(2);
        progress.setVocabularyGoal(20);
        progress.setGrammarCompleted(1);
        progress.setGrammarGoal(12);
        return progress;
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
