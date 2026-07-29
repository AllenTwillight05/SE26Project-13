package com.englishlearningcopilot.backend.service.impl;

import com.englishlearningcopilot.backend.dto.DailyLearningStatusResponse;
import com.englishlearningcopilot.backend.dto.DailyPracticeProgressResponse;
import com.englishlearningcopilot.backend.dto.LearningPlanRequest;
import com.englishlearningcopilot.backend.dto.LearningPlanResponse;
import com.englishlearningcopilot.backend.dto.ProfileSnapshotResponse;
import com.englishlearningcopilot.backend.entity.AppUser;
import com.englishlearningcopilot.backend.entity.SpeakingMessage;
import com.englishlearningcopilot.backend.entity.SpeakingMessageSender;
import com.englishlearningcopilot.backend.entity.SpeakingSession;
import com.englishlearningcopilot.backend.entity.UserDailyLearningProgress;
import com.englishlearningcopilot.backend.entity.UserLearningPlan;
import com.englishlearningcopilot.backend.entity.UserWordProgress;
import com.englishlearningcopilot.backend.exception.ResourceNotFoundException;
import com.englishlearningcopilot.backend.fsrs.FsrsRetention;
import com.englishlearningcopilot.backend.repository.SpeakingMessageRepository;
import com.englishlearningcopilot.backend.repository.SpeakingSessionRepository;
import com.englishlearningcopilot.backend.repository.UserDailyLearningProgressRepository;
import com.englishlearningcopilot.backend.repository.UserDailyPracticeLogRepository;
import com.englishlearningcopilot.backend.repository.UserLearningPlanRepository;
import com.englishlearningcopilot.backend.repository.UserRepository;
import com.englishlearningcopilot.backend.repository.UserWordProgressRepository;
import com.englishlearningcopilot.backend.service.LearningPlanService;
import com.englishlearningcopilot.backend.service.speech.OpenSpeakingMetrics;
import com.englishlearningcopilot.backend.service.speech.PronunciationScore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LearningPlanServiceImpl implements LearningPlanService {

    private static final String PRACTICE_TYPE_VOCABULARY = "VOCABULARY";
    private static final String PRACTICE_TYPE_GRAMMAR = "GRAMMAR";
    private static final String QUESTION_TYPE_VOCABULARY = "vocabulary";
    private static final String QUESTION_TYPE_GRAMMAR = "grammar";
    private static final int REVIEW_STATE = 1;

    private final UserRepository userRepository;
    private final UserLearningPlanRepository userLearningPlanRepository;
    private final UserDailyLearningProgressRepository userDailyLearningProgressRepository;
    private final UserDailyPracticeLogRepository userDailyPracticeLogRepository;
    private final SpeakingSessionRepository speakingSessionRepository;
    private final SpeakingMessageRepository speakingMessageRepository;
    private final UserWordProgressRepository userWordProgressRepository;
    private final ObjectMapper objectMapper;

    public LearningPlanServiceImpl(
            UserRepository userRepository,
            UserLearningPlanRepository userLearningPlanRepository,
            UserDailyLearningProgressRepository userDailyLearningProgressRepository,
            UserDailyPracticeLogRepository userDailyPracticeLogRepository,
            SpeakingSessionRepository speakingSessionRepository,
            SpeakingMessageRepository speakingMessageRepository,
            UserWordProgressRepository userWordProgressRepository,
            ObjectMapper objectMapper
    ) {
        this.userRepository = userRepository;
        this.userLearningPlanRepository = userLearningPlanRepository;
        this.userDailyLearningProgressRepository = userDailyLearningProgressRepository;
        this.userDailyPracticeLogRepository = userDailyPracticeLogRepository;
        this.speakingSessionRepository = speakingSessionRepository;
        this.speakingMessageRepository = speakingMessageRepository;
        this.userWordProgressRepository = userWordProgressRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional
    public LearningPlanResponse getLearningPlan(String username) {
        AppUser user = getCurrentUser(username);
        UserLearningPlan plan = getOrCreatePlan(user.getId());
        return toPlanResponse(plan);
    }

    @Override
    @Transactional
    public LearningPlanResponse updateLearningPlan(String username, LearningPlanRequest request) {
        AppUser user = getCurrentUser(username);
        UserLearningPlan plan = getOrCreatePlan(user.getId());
        plan.setDailyVocabularyGoal(request.dailyVocabularyGoal());
        plan.setDailyGrammarGoal(request.dailyGrammarGoal());
        plan.setEnabled(true);
        UserLearningPlan savedPlan = userLearningPlanRepository.save(plan);

        UserDailyLearningProgress todayProgress = getOrCreateTodayProgress(user.getId(), savedPlan);
        todayProgress.setVocabularyGoal(savedPlan.getDailyVocabularyGoal());
        todayProgress.setGrammarGoal(savedPlan.getDailyGrammarGoal());
        refreshCompletion(todayProgress);
        userDailyLearningProgressRepository.save(todayProgress);

        return toPlanResponse(savedPlan);
    }

    @Override
    @Transactional
    public DailyLearningStatusResponse getDailyStatus(String username) {
        AppUser user = getCurrentUser(username);
        return toDailyStatus(user.getId());
    }

    @Override
    @Transactional
    public DailyPracticeProgressResponse getVocabularyProgress(String username) {
        AppUser user = getCurrentUser(username);
        return toDailyStatus(user.getId()).vocabulary();
    }

    @Override
    @Transactional
    public DailyPracticeProgressResponse getGrammarProgress(String username) {
        AppUser user = getCurrentUser(username);
        return toDailyStatus(user.getId()).grammar();
    }

    @Override
    @Transactional
    public ProfileSnapshotResponse getProfileSnapshot(String username) {
        AppUser user = getCurrentUser(username);
        DailyLearningStatusResponse status = toDailyStatus(user.getId());

        List<ProfileSnapshotResponse.PlanItem> items = List.of(
                new ProfileSnapshotResponse.PlanItem(
                        "vocabulary",
                        "今日",
                        "词汇练习",
                        "已完成 " + status.vocabulary().completed() + " / " + status.vocabulary().total()
                                + "，待练 " + status.vocabulary().remaining(),
                        status.vocabulary().done()
                ),
                new ProfileSnapshotResponse.PlanItem(
                        "grammar",
                        "今日",
                        "语法练习",
                        "已完成 " + status.grammar().completed() + " / " + status.grammar().total()
                                + "，待练 " + status.grammar().remaining(),
                        status.grammar().done()
                )
        );

        Instant now = Instant.now();
        List<ProfileSnapshotResponse.ProgressMetric> progress = List.of(
                new ProfileSnapshotResponse.ProgressMetric(
                        "fluency",
                        "口语流利度",
                        weeklySpeakingReferenceScore(user.getId()),
                        "default"
                ),
                new ProfileSnapshotResponse.ProgressMetric(
                        "vocabulary-retention",
                        "词汇留存率",
                        fsrsRetentionRate(user.getId(), QUESTION_TYPE_VOCABULARY, now),
                        "teal"
                ),
                new ProfileSnapshotResponse.ProgressMetric(
                        "grammar-retention",
                        "语法留存率",
                        fsrsRetentionRate(user.getId(), QUESTION_TYPE_GRAMMAR, now),
                        "gold"
                )
        );

        ProfileSnapshotResponse.FeedbackSummary feedback = latestSpeakingFeedback(username);

        ProfileSnapshotResponse.ProfileDailyPlan dailyPlan = new ProfileSnapshotResponse.ProfileDailyPlan(
                true,
                status.allDone() ? "今日已完成" : "进行中",
                status.vocabulary().total(),
                status.grammar().total(),
                status.vocabulary(),
                status.grammar(),
                status.allDone(),
                items,
                progress
        );

        return new ProfileSnapshotResponse(
                displayName(user),
                "B1 -> B2",
                status.streakDays() + " 天",
                feedback,
                dailyPlan
        );
    }

    @Override
    @Transactional
    public void recordVocabularyPractice(Long userId, Long vocabularyId) {
        recordPractice(userId, today(), PRACTICE_TYPE_VOCABULARY, String.valueOf(vocabularyId));
    }

    @Override
    @Transactional
    public void recordGrammarPractice(Long userId, Integer grammarQuestionId) {
        recordPractice(userId, today(), PRACTICE_TYPE_GRAMMAR, String.valueOf(grammarQuestionId));
    }

    @Override
    @Transactional
    public void recordOutboxPractice(Long userId, LocalDate planDate, String practiceType, String itemId) {
        recordPractice(userId, planDate, practiceType, itemId);
    }

    private void recordPractice(Long userId, LocalDate planDate, String practiceType, String itemId) {

        if (userDailyPracticeLogRepository.insertIfAbsent(
                userId,
                planDate,
                practiceType,
                itemId
        ) == 0) {
            return;
        }

        UserLearningPlan plan = getOrCreatePlan(userId);
        getOrCreateProgress(userId, plan, planDate);

        if (PRACTICE_TYPE_VOCABULARY.equals(practiceType)) {
            userDailyLearningProgressRepository.incrementVocabularyCompletion(userId, planDate);
        } else if (PRACTICE_TYPE_GRAMMAR.equals(practiceType)) {
            userDailyLearningProgressRepository.incrementGrammarCompletion(userId, planDate);
        }
    }

    private DailyLearningStatusResponse toDailyStatus(Long userId) {
        UserLearningPlan plan = getOrCreatePlan(userId);
        UserDailyLearningProgress progress = getOrCreateTodayProgress(userId, plan);
        refreshCompletion(progress);
        userDailyLearningProgressRepository.save(progress);

        DailyPracticeProgressResponse vocabulary = toVocabularyProgress(progress);
        DailyPracticeProgressResponse grammar = toGrammarProgress(progress);
        boolean allDone = vocabulary.done() && grammar.done();

        return new DailyLearningStatusResponse(today(), vocabulary, grammar, allDone, calculateStreakDays(userId));
    }

    private UserLearningPlan getOrCreatePlan(Long userId) {
        return userLearningPlanRepository.findByUserId(userId)
                .orElseGet(() -> createDefaultPlan(userId));
    }

    private UserLearningPlan createDefaultPlan(Long userId) {
        userLearningPlanRepository.insertDefaultIfAbsent(
                userId,
                UserLearningPlan.DEFAULT_VOCABULARY_GOAL,
                UserLearningPlan.DEFAULT_GRAMMAR_GOAL
        );
        return userLearningPlanRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalStateException("Learning plan was not created."));
    }

    private UserDailyLearningProgress getOrCreateTodayProgress(Long userId, UserLearningPlan plan) {
        return getOrCreateProgress(userId, plan, today());
    }

    private UserDailyLearningProgress getOrCreateProgress(Long userId, UserLearningPlan plan, LocalDate planDate) {
        userDailyLearningProgressRepository.insertIfAbsent(
                userId,
                planDate,
                plan.getDailyVocabularyGoal(),
                plan.getDailyGrammarGoal()
        );
        return userDailyLearningProgressRepository.findByUserIdAndPlanDate(userId, planDate)
                .orElseThrow(() -> new IllegalStateException("Learning progress was not created."));
    }

    private void refreshCompletion(UserDailyLearningProgress progress) {
        boolean wasCompleted = progress.isCompleted();
        boolean isCompleted = progress.getVocabularyCompleted() >= progress.getVocabularyGoal()
                && progress.getGrammarCompleted() >= progress.getGrammarGoal();
        progress.setCompleted(isCompleted);

        if (!wasCompleted && isCompleted) {
            progress.setCompletedAt(Instant.now());
        }

        if (!isCompleted) {
            progress.setCompletedAt(null);
        }
    }

    private DailyPracticeProgressResponse toVocabularyProgress(UserDailyLearningProgress progress) {
        return toPracticeProgress(progress.getVocabularyCompleted(), progress.getVocabularyGoal());
    }

    private DailyPracticeProgressResponse toGrammarProgress(UserDailyLearningProgress progress) {
        return toPracticeProgress(progress.getGrammarCompleted(), progress.getGrammarGoal());
    }

    private DailyPracticeProgressResponse toPracticeProgress(int completed, int total) {
        int remaining = Math.max(total - completed, 0);
        return new DailyPracticeProgressResponse(completed, total, remaining, completed >= total);
    }

    private LearningPlanResponse toPlanResponse(UserLearningPlan plan) {
        return new LearningPlanResponse(
                plan.getDailyVocabularyGoal(),
                plan.getDailyGrammarGoal(),
                plan.isEnabled()
        );
    }

    private int calculateStreakDays(Long userId) {
        List<UserDailyLearningProgress> completedDays =
                userDailyLearningProgressRepository.findByUserIdAndCompletedTrueOrderByPlanDateDesc(userId);
        LocalDate expectedDate = toDailyStatusStartDate(userId);
        int streak = 0;

        for (UserDailyLearningProgress progress : completedDays) {
            if (progress.getPlanDate().equals(expectedDate)) {
                streak++;
                expectedDate = expectedDate.minusDays(1);
            } else if (progress.getPlanDate().isBefore(expectedDate)) {
                break;
            }
        }

        return streak;
    }

    private LocalDate toDailyStatusStartDate(Long userId) {
        return userDailyLearningProgressRepository.findByUserIdAndPlanDate(userId, today())
                .filter(UserDailyLearningProgress::isCompleted)
                .map(UserDailyLearningProgress::getPlanDate)
                .orElse(today().minusDays(1));
    }

    private int weeklySpeakingReferenceScore(Long userId) {
        ZoneId zone = ZoneId.systemDefault();
        LocalDate weekStart = LocalDate.now(zone).with(DayOfWeek.MONDAY);
        Instant weekStartInstant = weekStart.atStartOfDay(zone).toInstant();
        Instant weekEndExclusiveInstant = weekStart.plusDays(7).atStartOfDay(zone).toInstant();

        return toDisplayScore(speakingMessageRepository
                .findBySessionUserIdAndSenderAndCreatedAtBetween(
                        userId,
                        SpeakingMessageSender.USER,
                        weekStartInstant,
                        weekEndExclusiveInstant
                )
                .stream()
                .filter(message -> message.getPronunciationScore() != null)
                .map(this::readPronunciationScore)
                .mapToDouble(OpenSpeakingMetrics::referenceScore)
                .average()
                .orElse(0));
    }

    private int fsrsRetentionRate(Long userId, String questionType, Instant now) {
        List<UserWordProgress> reviewCards = userWordProgressRepository
                .findByUserIdAndQuestionType(userId, questionType)
                .stream()
                .filter(progress -> progress.getState() != null && progress.getState() == REVIEW_STATE)
                .toList();
        return FsrsRetention.averagePercent(reviewCards, now);
    }

    private ProfileSnapshotResponse.FeedbackSummary latestSpeakingFeedback(String username) {
        for (SpeakingSession session : speakingSessionRepository.findByUserUsernameOrderByStartedAtDesc(username)) {
            List<SpeakingMessage> userMessages = speakingMessageRepository
                    .findBySessionIdOrderByTurnIndexAscCreatedAtAsc(session.getId())
                    .stream()
                    .filter(message -> message.getSender() == SpeakingMessageSender.USER)
                    .filter(message -> message.getPronunciationScore() != null)
                    .toList();

            if (userMessages.isEmpty()) {
                continue;
            }

            List<PronunciationScore> scores = userMessages.stream()
                    .map(this::readPronunciationScore)
                    .toList();
            String issueSentence = userMessages.stream()
                    .filter(message -> OpenSpeakingMetrics.referenceScore(readPronunciationScore(message)) < 60)
                    .map(SpeakingMessage::getContent)
                    .filter(content -> content != null && !content.isBlank())
                    .findFirst()
                    .orElse("无");

            return new ProfileSnapshotResponse.FeedbackSummary(
                    "Ready",
                    "",
                    List.of(),
                    List.of(),
                    session.getScenario().getTitle(),
                    feedbackTime(session),
                    toDisplayScore(scores.stream().mapToDouble(OpenSpeakingMetrics::referenceScore).average().orElse(0)),
                    toDisplayScore(scores.stream().mapToDouble(PronunciationScore::accuracy).average().orElse(0)),
                    toDisplayScore(scores.stream().mapToDouble(PronunciationScore::fluency).average().orElse(0)),
                    toDisplayScore(scores.stream().mapToDouble(PronunciationScore::integrity).average().orElse(0)),
                    formatSpeed(OpenSpeakingMetrics.wordsPerMinute(userMessages)),
                    issueSentence
            );
        }

        return new ProfileSnapshotResponse.FeedbackSummary(
                "No speaking feedback",
                "",
                List.of(),
                List.of(),
                "暂无口语练习",
                "",
                null,
                null,
                null,
                null,
                null,
                "无"
        );
    }

    private PronunciationScore readPronunciationScore(SpeakingMessage message) {
        if (message.getPronunciationDetail() != null && !message.getPronunciationDetail().isBlank()) {
            try {
                return objectMapper.readValue(message.getPronunciationDetail(), PronunciationScore.class);
            } catch (JsonProcessingException ignored) {
                // Fall back to the stored total score below.
            }
        }
        double total = message.getPronunciationScore() != null ? message.getPronunciationScore() : 0;
        return new PronunciationScore(total, total, total, total, 0);
    }

    private int toDisplayScore(double value) {
        return (int) Math.round(value);
    }

    private String formatSpeed(double speed) {
        if (speed <= 0) {
            return "暂无";
        }
        return Math.round(speed) + " WPM";
    }

    private String feedbackTime(SpeakingSession session) {
        Instant time = session.getCompletedAt() != null
                ? session.getCompletedAt()
                : session.getUpdatedAt() != null ? session.getUpdatedAt() : session.getStartedAt();
        return time != null ? time.toString() : "";
    }

    private AppUser getCurrentUser(String username) {
        if (username == null) {
            throw new BadCredentialsException("Authentication is required.");
        }

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Current user was not found."));
    }

    private String displayName(AppUser user) {
        return user.getDisplayName() == null || user.getDisplayName().isBlank()
                ? user.getUsername()
                : user.getDisplayName();
    }

    private LocalDate today() {
        return LocalDate.now(ZoneId.systemDefault());
    }
}
