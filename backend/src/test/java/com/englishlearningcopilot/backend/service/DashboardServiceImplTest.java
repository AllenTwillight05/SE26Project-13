package com.englishlearningcopilot.backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.englishlearningcopilot.backend.dto.DailyLearningStatusResponse;
import com.englishlearningcopilot.backend.dto.DailyPracticeProgressResponse;
import com.englishlearningcopilot.backend.dto.DashboardCommunityLearningTrendsResponse;
import com.englishlearningcopilot.backend.dto.DashboardWeeklyOverviewResponse;
import com.englishlearningcopilot.backend.entity.AppUser;
import com.englishlearningcopilot.backend.entity.SpeakingMessage;
import com.englishlearningcopilot.backend.entity.SpeakingMessageSender;
import com.englishlearningcopilot.backend.entity.SpeakingScenario;
import com.englishlearningcopilot.backend.entity.UserRole;
import com.englishlearningcopilot.backend.entity.Vocabulary;
import com.englishlearningcopilot.backend.exception.ResourceNotFoundException;
import com.englishlearningcopilot.backend.repository.GrammarLeaderboardProjection;
import com.englishlearningcopilot.backend.repository.SpeakingLeaderboardProjection;
import com.englishlearningcopilot.backend.repository.SpeakingMessageRepository;
import com.englishlearningcopilot.backend.repository.SpeakingScenarioRepository;
import com.englishlearningcopilot.backend.repository.SpeakingScenarioScoreProjection;
import com.englishlearningcopilot.backend.repository.SpeakingSessionRepository;
import com.englishlearningcopilot.backend.repository.UserDailyLearningProgressRepository;
import com.englishlearningcopilot.backend.repository.UserDailyPracticeLogRepository;
import com.englishlearningcopilot.backend.repository.UserGrammarbookRepository;
import com.englishlearningcopilot.backend.repository.UserRepository;
import com.englishlearningcopilot.backend.repository.UserWordbookRepository;
import com.englishlearningcopilot.backend.repository.VocabularyLeaderboardProjection;
import com.englishlearningcopilot.backend.repository.VocabularyRepository;
import com.englishlearningcopilot.backend.service.impl.DashboardServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class DashboardServiceImplTest {

    @Mock
    private SpeakingScenarioRepository speakingScenarioRepository;

    @Mock
    private SpeakingSessionRepository speakingSessionRepository;

    @Mock
    private SpeakingMessageRepository speakingMessageRepository;

    @Mock
    private UserGrammarbookRepository userGrammarbookRepository;

    @Mock
    private UserWordbookRepository userWordbookRepository;

    @Mock
    private VocabularyRepository vocabularyRepository;

    @Mock
    private LearningPlanService learningPlanService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserDailyLearningProgressRepository userDailyLearningProgressRepository;

    @Mock
    private UserDailyPracticeLogRepository userDailyPracticeLogRepository;

    @Test
    void getCommunityLearningTrendsCombinesSpeakingVocabularyAndGrammarLeaderboards() {
        DashboardServiceImpl service = service();
        SpeakingLeaderboardProjection speaking = speakingProjection("airport", "Airport", null, 5L);
        VocabularyLeaderboardProjection vocabularyProjection = vocabularyProjection(10L, 8L);
        GrammarLeaderboardProjection grammar = grammarProjection("Tense", 6L);
        when(speakingSessionRepository.findSpeakingLeaderboard(any(Pageable.class))).thenReturn(List.of(speaking));
        when(userWordbookRepository.findVocabularyLeaderboard(any(Pageable.class))).thenReturn(List.of(vocabularyProjection));
        when(vocabularyRepository.findAllById(List.of(10L))).thenReturn(List.of(vocabulary(10L, "accept", null)));
        when(userGrammarbookRepository.findGrammarLeaderboard(any(Pageable.class))).thenReturn(List.of(grammar));

        DashboardCommunityLearningTrendsResponse response = service.getCommunityLearningTrends();

        assertThat(response.speaking()).hasSize(1);
        assertThat(response.speaking().get(0).rank()).isEqualTo(1);
        assertThat(response.speaking().get(0).description()).isEmpty();
        assertThat(response.vocabulary()).hasSize(1);
        assertThat(response.vocabulary().get(0).briefTranslation()).isEmpty();
        assertThat(response.grammar()).hasSize(1);
    }

    @Test
    void getCommunityLearningTrendsSkipsVocabularyLeaderboardRowsWithoutVocabularyDetails() {
        DashboardServiceImpl service = service();
        when(speakingSessionRepository.findSpeakingLeaderboard(any(Pageable.class))).thenReturn(List.of());
        when(userWordbookRepository.findVocabularyLeaderboard(any(Pageable.class)))
                .thenReturn(List.of(vocabularyProjection(10L, 8L)));
        when(vocabularyRepository.findAllById(List.of(10L))).thenReturn(List.of());
        when(userGrammarbookRepository.findGrammarLeaderboard(any(Pageable.class))).thenReturn(List.of());

        DashboardCommunityLearningTrendsResponse response = service.getCommunityLearningTrends();

        assertThat(response.vocabulary()).isEmpty();
    }

    @Test
    void getStudyPlanDelegatesToLearningPlanService() {
        DashboardServiceImpl service = service();
        when(learningPlanService.getDailyStatus("learner")).thenReturn(new DailyLearningStatusResponse(
                LocalDate.now(),
                new DailyPracticeProgressResponse(1, 2, 1, false),
                new DailyPracticeProgressResponse(2, 2, 0, true),
                false,
                3
        ));

        var response = service.getStudyPlan("learner");

        assertThat(response.streakDays()).isEqualTo(3);
        assertThat(response.grammar().done()).isTrue();
    }

    @Test
    void getRecommendedTaskReturnsWeakestPracticedDailyScenario() {
        DashboardServiceImpl service = service();
        AppUser user = user(7L, "learner");
        when(userRepository.findByUsername("learner")).thenReturn(Optional.of(user));
        when(speakingSessionRepository.findWeakestDailyScenarioByUserId(eq(7L), any(Pageable.class)))
                .thenReturn(List.of(scoreProjection("G-10-phone-call", "电话沟通", null, null, 58.5)));

        var response = service.getRecommendedTask("learner");

        assertThat(response.scenarioId()).isEqualTo("G-10-phone-call");
        assertThat(response.topic()).isEqualTo("电话沟通");
        assertThat(response.suggestedDuration()).isEqualTo("12 min");
        assertThat(response.intensity()).isEqualTo("Daily");
        assertThat(response.score()).isEqualTo(58.5);
        assertThat(response.route()).isEqualTo("/speaking/G-10-phone-call");
    }

    @Test
    void getRecommendedTaskUsesStableDailyScenarioWhenUserHasNoScores() {
        DashboardServiceImpl service = service();
        AppUser user = user(7L, "learner");
        when(userRepository.findByUsername("learner")).thenReturn(Optional.of(user));
        when(speakingSessionRepository.findWeakestDailyScenarioByUserId(eq(7L), any(Pageable.class)))
                .thenReturn(List.of());
        List<SpeakingScenario> scenarios = List.of(
                scenario("G-01-airport", "机场值机"),
                scenario("G-02-restaurant", "餐厅点餐"),
                scenario("G-10-phone-call", "电话沟通")
        );
        when(speakingScenarioRepository.findByActiveTrueAndIdStartingWithOrderByIdAsc("G-"))
                .thenReturn(scenarios);

        var first = service.getRecommendedTask("learner");
        var second = service.getRecommendedTask("learner");

        assertThat(first.scenarioId()).isEqualTo(second.scenarioId());
        assertThat(scenarios).extracting(SpeakingScenario::getId).contains(first.scenarioId());
        assertThat(first.score()).isNull();
    }

    @Test
    void getRecommendedTaskRejectsMissingDailyScenarios() {
        DashboardServiceImpl service = service();
        AppUser user = user(7L, "learner");
        when(userRepository.findByUsername("learner")).thenReturn(Optional.of(user));
        when(speakingSessionRepository.findWeakestDailyScenarioByUserId(eq(7L), any(Pageable.class)))
                .thenReturn(List.of());
        when(speakingScenarioRepository.findByActiveTrueAndIdStartingWithOrderByIdAsc("G-"))
                .thenReturn(List.of());

        assertThatThrownBy(() -> service.getRecommendedTask("learner"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("No daily speaking scenario is available.");
    }

    @Test
    void getWeeklyOverviewUsesOpenSpeakingMetricsFromCurrentWeekMessages() {
        DashboardServiceImpl service = service();
        AppUser user = user(7L, "learner");
        when(userRepository.findByUsername("learner")).thenReturn(Optional.of(user));
        when(userDailyLearningProgressRepository.findLearningDatesInRange(eq(7L), any(), any()))
                .thenReturn(List.of(LocalDate.now().with(java.time.DayOfWeek.MONDAY)));
        when(userDailyPracticeLogRepository.countByUserIdAndPlanDateBetweenAndPracticeType(eq(7L), any(), any(), eq("VOCABULARY")))
                .thenReturn(3L);
        when(userDailyPracticeLogRepository.countByUserIdAndPlanDateBetweenAndPracticeType(eq(7L), any(), any(), eq("GRAMMAR")))
                .thenReturn(4L);
        when(speakingMessageRepository.findBySessionUserIdAndSenderAndCreatedAtBetween(
                eq(7L),
                eq(SpeakingMessageSender.USER),
                any(Instant.class),
                any(Instant.class)
        )).thenReturn(List.of(
                userMessage(60_000L, 80, 90, LocalDate.now().with(java.time.DayOfWeek.MONDAY).plusDays(1)),
                userMessage(30_000L, 100, 90, LocalDate.now().with(java.time.DayOfWeek.MONDAY).plusDays(1))
        ));

        DashboardWeeklyOverviewResponse overview = service.getWeeklyOverview("learner");

        assertThat(overview.speakingDuration()).isEqualTo("2 min");
        assertThat(overview.pronunciationReference()).isEqualTo("87 / 100");
        assertThat(overview.learningDays()).isEqualTo("2 天");
        assertThat(overview.vocabularyLearned()).isEqualTo("3 词");
        assertThat(overview.grammarPracticed()).isEqualTo("4 题");
    }

    @Test
    void getWeeklyOverviewFormatsNoSpeakingDataAsZeroAndDash() {
        DashboardServiceImpl service = service();
        AppUser user = user(7L, "learner");
        when(userRepository.findByUsername("learner")).thenReturn(Optional.of(user));
        when(userDailyLearningProgressRepository.findLearningDatesInRange(eq(7L), any(), any())).thenReturn(List.of());
        when(userDailyPracticeLogRepository.countByUserIdAndPlanDateBetweenAndPracticeType(eq(7L), any(), any(), any()))
                .thenReturn(0L);
        when(speakingMessageRepository.findBySessionUserIdAndSenderAndCreatedAtBetween(
                eq(7L),
                eq(SpeakingMessageSender.USER),
                any(Instant.class),
                any(Instant.class)
        )).thenReturn(List.of());

        DashboardWeeklyOverviewResponse overview = service.getWeeklyOverview("learner");

        assertThat(overview.speakingDuration()).isEqualTo("0 min");
        assertThat(overview.pronunciationReference()).isEqualTo("-");
        assertThat(overview.learningDays()).contains("0");
    }

    @Test
    void getWeeklyOverviewFallsBackToStoredScoreWhenPronunciationDetailIsInvalid() {
        DashboardServiceImpl service = service();
        AppUser user = user(7L, "learner");
        SpeakingMessage message = new SpeakingMessage();
        message.setSender(SpeakingMessageSender.USER);
        message.setDurationMs(1_000L);
        message.setPronunciationScore(76.0);
        message.setPronunciationDetail("{bad json");
        ReflectionTestUtils.setField(
                message,
                "createdAt",
                LocalDate.now().atStartOfDay(java.time.ZoneId.systemDefault()).toInstant()
        );
        when(userRepository.findByUsername("learner")).thenReturn(Optional.of(user));
        when(userDailyLearningProgressRepository.findLearningDatesInRange(eq(7L), any(), any())).thenReturn(List.of());
        when(userDailyPracticeLogRepository.countByUserIdAndPlanDateBetweenAndPracticeType(eq(7L), any(), any(), any()))
                .thenReturn(0L);
        when(speakingMessageRepository.findBySessionUserIdAndSenderAndCreatedAtBetween(
                eq(7L),
                eq(SpeakingMessageSender.USER),
                any(Instant.class),
                any(Instant.class)
        )).thenReturn(List.of(message));

        DashboardWeeklyOverviewResponse overview = service.getWeeklyOverview("learner");

        assertThat(overview.speakingDuration()).isEqualTo("1 min");
        assertThat(overview.pronunciationReference()).isEqualTo("76 / 100");
    }

    @Test
    void getWeeklyOverviewIgnoresNullMessageFieldsAndUsesFallbackScore() {
        DashboardServiceImpl service = service();
        AppUser user = user(7L, "learner");
        SpeakingMessage nullFields = new SpeakingMessage();
        nullFields.setSender(SpeakingMessageSender.USER);
        nullFields.setDurationMs(null);
        nullFields.setPronunciationScore(null);
        SpeakingMessage fallbackScore = new SpeakingMessage();
        fallbackScore.setSender(SpeakingMessageSender.USER);
        fallbackScore.setDurationMs(-1L);
        fallbackScore.setPronunciationScore(64.0);
        fallbackScore.setPronunciationDetail(null);
        when(userRepository.findByUsername("learner")).thenReturn(Optional.of(user));
        when(userDailyLearningProgressRepository.findLearningDatesInRange(eq(7L), any(), any()))
                .thenReturn(List.of());
        when(userDailyPracticeLogRepository.countByUserIdAndPlanDateBetweenAndPracticeType(eq(7L), any(), any(), any()))
                .thenReturn(0L);
        when(speakingMessageRepository.findBySessionUserIdAndSenderAndCreatedAtBetween(
                eq(7L),
                eq(SpeakingMessageSender.USER),
                any(Instant.class),
                any(Instant.class)
        )).thenReturn(List.of(nullFields, fallbackScore));

        DashboardWeeklyOverviewResponse overview = service.getWeeklyOverview("learner");

        assertThat(overview.speakingDuration()).isEqualTo("0 min");
        assertThat(overview.pronunciationReference()).isEqualTo("64 / 100");
        assertThat(overview.learningDays()).contains("0");
    }

    @Test
    void getWeeklyOverviewRequiresAuthentication() {
        DashboardServiceImpl service = service();

        assertThatThrownBy(() -> service.getWeeklyOverview(null))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessage("Authentication is required.");
    }

    @Test
    void getWeeklyOverviewRejectsMissingUser() {
        DashboardServiceImpl service = service();
        when(userRepository.findByUsername("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getWeeklyOverview("missing"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Current user was not found.");
    }

    private DashboardServiceImpl service() {
        return new DashboardServiceImpl(
                speakingScenarioRepository,
                speakingSessionRepository,
                speakingMessageRepository,
                userGrammarbookRepository,
                userWordbookRepository,
                vocabularyRepository,
                learningPlanService,
                userRepository,
                userDailyLearningProgressRepository,
                userDailyPracticeLogRepository,
                new ObjectMapper()
        );
    }

    private static SpeakingMessage userMessage(long durationMs, double totalScore, double accuracy, LocalDate date) {
        SpeakingMessage message = new SpeakingMessage();
        message.setSender(SpeakingMessageSender.USER);
        message.setDurationMs(durationMs);
        message.setContent("I would like to discuss the project plan.");
        message.setPronunciationScore(totalScore);
        message.setPronunciationDetail("""
                {"totalScore":%s,"accuracy":%s,"fluency":80,"integrity":80,"speed":0}
                """.formatted(totalScore, accuracy));
        ReflectionTestUtils.setField(message, "createdAt", date.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant());
        return message;
    }

    private static AppUser user(Long id, String username) {
        AppUser user = new AppUser();
        ReflectionTestUtils.setField(user, "id", id);
        user.setUsername(username);
        user.setEmail(username + "@example.com");
        user.setDisplayName(username);
        user.setPasswordHash("hashed-password");
        user.setRole(UserRole.USER);
        user.setEnabled(true);
        return user;
    }

    private static Vocabulary vocabulary(Long id, String word, String briefTranslation) {
        Vocabulary vocabulary = new Vocabulary();
        vocabulary.setId(id);
        vocabulary.setWord(word);
        vocabulary.setBriefTranslation(briefTranslation);
        return vocabulary;
    }

    private static SpeakingScenario scenario(String id, String title) {
        SpeakingScenario scenario = new SpeakingScenario();
        scenario.setId(id);
        scenario.setTitle(title);
        scenario.setDescription(title + " description");
        scenario.setDifficulty("B1");
        scenario.setAccent("US");
        scenario.setDuration("12 min");
        scenario.setSummary(title + " summary");
        scenario.setTone("friendly");
        scenario.setGoal(title + " goal");
        scenario.setKeywords("daily");
        scenario.setRolePrompt("role");
        scenario.setOpeningMessage("hello");
        scenario.setTargetTurns(5);
        scenario.setScoringRubric("rubric");
        return scenario;
    }

    private static SpeakingLeaderboardProjection speakingProjection(
            String scenarioId,
            String topic,
            String description,
            Long learnerCount
    ) {
        return new SpeakingLeaderboardProjection() {
            @Override
            public String getScenarioId() {
                return scenarioId;
            }

            @Override
            public String getTopic() {
                return topic;
            }

            @Override
            public String getDescription() {
                return description;
            }

            @Override
            public Long getLearnerCount() {
                return learnerCount;
            }
        };
    }

    private static SpeakingScenarioScoreProjection scoreProjection(
            String scenarioId,
            String title,
            String duration,
            String difficulty,
            Double averageScore
    ) {
        return new SpeakingScenarioScoreProjection() {
            @Override
            public String getScenarioId() {
                return scenarioId;
            }

            @Override
            public String getTitle() {
                return title;
            }

            @Override
            public String getDuration() {
                return duration;
            }

            @Override
            public String getDifficulty() {
                return difficulty;
            }

            @Override
            public Double getAverageScore() {
                return averageScore;
            }
        };
    }

    private static VocabularyLeaderboardProjection vocabularyProjection(Long vocabularyId, Long learnerCount) {
        return new VocabularyLeaderboardProjection() {
            @Override
            public Long getVocabularyId() {
                return vocabularyId;
            }

            @Override
            public Long getLearnerCount() {
                return learnerCount;
            }
        };
    }

    private static GrammarLeaderboardProjection grammarProjection(String category, Long learnerCount) {
        return new GrammarLeaderboardProjection() {
            @Override
            public String getGrammarCategory() {
                return category;
            }

            @Override
            public Long getLearnerCount() {
                return learnerCount;
            }
        };
    }
}
