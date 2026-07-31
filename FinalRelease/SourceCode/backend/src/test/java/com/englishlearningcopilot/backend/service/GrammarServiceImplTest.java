package com.englishlearningcopilot.backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.englishlearningcopilot.backend.dto.DailyPracticeProgressResponse;
import com.englishlearningcopilot.backend.dto.GrammarFavoriteRequest;
import com.englishlearningcopilot.backend.dto.GrammarFavoriteResponse;
import com.englishlearningcopilot.backend.dto.GrammarPracticeQuestionResponse;
import com.englishlearningcopilot.backend.dto.GrammarPracticeResultRequest;
import com.englishlearningcopilot.backend.dto.GrammarRatingRequest;
import com.englishlearningcopilot.backend.dto.GrammarOverviewResponse;
import com.englishlearningcopilot.backend.dto.GrammarTopicResponse;
import com.englishlearningcopilot.backend.entity.AppUser;
import com.englishlearningcopilot.backend.entity.GrammarQuestion;
import com.englishlearningcopilot.backend.entity.UserGrammarbook;
import com.englishlearningcopilot.backend.entity.UserWordProgress;
import com.englishlearningcopilot.backend.entity.UserRole;
import com.englishlearningcopilot.backend.exception.ResourceNotFoundException;
import com.englishlearningcopilot.backend.repository.GrammarQuestionRepository;
import com.englishlearningcopilot.backend.repository.UserGrammarbookRepository;
import com.englishlearningcopilot.backend.repository.UserRepository;
import com.englishlearningcopilot.backend.repository.UserWordProgressRepository;
import com.englishlearningcopilot.backend.service.impl.GrammarServiceImpl;
import com.englishlearningcopilot.backend.service.agent.GrammarTutorAgentClient;
import com.englishlearningcopilot.backend.dto.GrammarTutorMessage;
import com.englishlearningcopilot.backend.dto.GrammarTutorRequest;
import com.englishlearningcopilot.backend.dto.GrammarTutorResponse;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class GrammarServiceImplTest {

    @Mock
    private GrammarQuestionRepository grammarQuestionRepository;

    @Mock
    private UserGrammarbookRepository userGrammarbookRepository;

    @Mock
    private UserWordProgressRepository userWordProgressRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private LearningPlanService learningPlanService;

    @Mock
    private ReviewService reviewService;

    @Mock
    private LearningProgressOutboxService learningProgressOutboxService;

    @Mock
    private GrammarTutorAgentClient grammarTutorAgentClient;

    @InjectMocks
    private GrammarServiceImpl grammarService;

    @Test
    void getPracticeQuestionsReturnsUnpracticedQuestionsForUserAndCategory() {
        AppUser user = user(7L, "learner");
        when(userRepository.findByUsername("learner")).thenReturn(Optional.of(user));
        when(grammarQuestionRepository.findRandomUnpracticedQuestionsByCategory(eq(7L), eq("Tense"), any(Pageable.class)))
                .thenReturn(List.of(question(1, "Tense")));

        List<GrammarPracticeQuestionResponse> questions = grammarService.getPracticeQuestions("learner", "Tense");

        assertThat(questions).extracting(GrammarPracticeQuestionResponse::id).containsExactly(1);
        verify(grammarQuestionRepository)
                .findRandomUnpracticedQuestionsByCategory(eq(7L), eq("Tense"), any(Pageable.class));
    }

    @Test
    void getOverviewForAnonymousUserUsesCompletedQuestionFallback() {
        when(grammarQuestionRepository.findAll()).thenReturn(List.of(
                question(1, "Tense"),
                question(2, "Clause")
        ));

        GrammarOverviewResponse response = grammarService.getOverview(null);

        assertThat(response.masteryRate()).isZero();
        assertThat(response.stats()).hasSize(3);
        verify(userWordProgressRepository, never()).findByUserIdAndQuestionType(any(), any());
    }

    @Test
    void getOverviewForReviewCardsUsesRetentionAndDueCount() {
        AppUser user = user(7L, "learner");
        UserWordProgress dueReview = progress("1", 1, 1, Instant.now().minusSeconds(3600), 2.5);
        UserWordProgress futureReview = progress("2", 1, 1, Instant.now().plusSeconds(3600), 5.0);
        when(grammarQuestionRepository.findAll()).thenReturn(List.of(question(1, "Tense"), question(2, "Tense")));
        when(userRepository.findByUsername("learner")).thenReturn(Optional.of(user));
        when(userWordProgressRepository.findByUserIdAndQuestionType(7L, "grammar"))
                .thenReturn(List.of(dueReview, futureReview));

        GrammarOverviewResponse response = grammarService.getOverview("learner");

        assertThat(response.masteryRate()).isBetween(1, 100);
        assertThat(response.stats().get(0).value()).contains("2");
        assertThat(response.stats().get(1).value()).contains("1");
    }

    @Test
    void getOverviewHandlesIncompleteRowsAndEmptyQuestionBank() {
        AppUser user = user(7L, "learner");
        UserWordProgress incomplete = progress("1", null, null, null, null);
        incomplete.setLastReview(null);
        UserWordProgress futureReview = progress("2", 1, 1, Instant.now().plusSeconds(3600), 5.0);
        when(grammarQuestionRepository.findAll()).thenReturn(List.of());
        when(userRepository.findByUsername("learner")).thenReturn(Optional.of(user));
        when(userWordProgressRepository.findByUserIdAndQuestionType(7L, "grammar"))
                .thenReturn(List.of(incomplete, futureReview));

        GrammarOverviewResponse response = grammarService.getOverview("learner");

        assertThat(response.masteryRate()).isBetween(0, 100);
        assertThat(response.stats().get(0).value()).contains("1");
        assertThat(response.stats().get(1).value()).contains("0");
    }

    @Test
    void getOverviewCountsCompletedRowsFromRepsAndIgnoresNonReviewCards() {
        AppUser user = user(7L, "learner");
        UserWordProgress completedByReps = progress("1", 1, 0, Instant.now().minusSeconds(60), 2.5);
        completedByReps.setLastReview(null);
        UserWordProgress completedByLastReview = progress("3", null, 0, null, null);
        UserWordProgress nullState = progress("2", 1, null, Instant.now().minusSeconds(60), 2.5);
        when(grammarQuestionRepository.findAll()).thenReturn(List.of(
                question(1, "Tense"),
                question(2, "Tense"),
                question(3, "Tense")
        ));
        when(userRepository.findByUsername("learner")).thenReturn(Optional.of(user));
        when(userWordProgressRepository.findByUserIdAndQuestionType(7L, "grammar"))
                .thenReturn(List.of(completedByReps, completedByLastReview, nullState));

        GrammarOverviewResponse response = grammarService.getOverview("learner");

        assertThat(response.masteryRate()).isEqualTo(100);
        assertThat(response.stats().get(0).value()).contains("0");
    }

    @Test
    void getTopicsForAnonymousUserIgnoresProgressAndBlankExamples() {
        GrammarQuestion blankExample = question(1, "Topic");
        blankExample.setQuestionText(" ");
        GrammarQuestion nullExample = question(3, "Topic");
        nullExample.setQuestionText(null);
        GrammarQuestion namedExample = question(2, "Topic");
        namedExample.setQuestionText("Useful example");
        when(grammarQuestionRepository.findAll()).thenReturn(List.of(blankExample, nullExample, namedExample));

        List<GrammarTopicResponse> topics = grammarService.getTopics(null);

        assertThat(topics).hasSize(1);
        assertThat(topics.get(0).progress()).isZero();
        assertThat(topics.get(0).examples()).containsExactly("Useful example");
    }

    @Test
    void getOverviewReturnsZeroProgressWhenNoQuestionsOrReviewCardsExist() {
        AppUser user = user(7L, "learner");
        UserWordProgress notCompleted = progress("1", null, 0, null, null);
        notCompleted.setLastReview(null);
        when(grammarQuestionRepository.findAll()).thenReturn(List.of());
        when(userRepository.findByUsername("learner")).thenReturn(Optional.of(user));
        when(userWordProgressRepository.findByUserIdAndQuestionType(7L, "grammar"))
                .thenReturn(List.of(notCompleted));

        GrammarOverviewResponse response = grammarService.getOverview("learner");

        assertThat(response.masteryRate()).isZero();
        assertThat(response.stats().get(1).value()).contains("0");
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "从句",
            "时态与语态",
            "词汇与逻辑",
            "非谓语动词",
            "介词与固定搭配",
            "代词与限定词",
            "主谓一致",
            "情态动词与虚拟语气"
    })
    void getTopicsReturnsSpecializedSummariesForKnownCategories(String category) {
        when(grammarQuestionRepository.findAll()).thenReturn(List.of(question(1, category)));

        List<GrammarTopicResponse> topics = grammarService.getTopics(null);

        assertThat(topics).hasSize(1);
        assertThat(topics.get(0).summary()).isNotBlank();
    }

    @Test
    void getTopicsReturnsSortedCategoriesWithCompletedProgressAndExamples() {
        AppUser user = user(7L, "learner");
        UserWordProgress completed = progress("2", 1, 0, Instant.now(), 2.5);
        UserWordProgress invalidQuestionId = progress("not-a-number", 1, 0, Instant.now(), 2.5);
        when(userRepository.findByUsername("learner")).thenReturn(Optional.of(user));
        when(userWordProgressRepository.findByUserIdAndQuestionType(7L, "grammar"))
                .thenReturn(List.of(completed, invalidQuestionId));
        when(grammarQuestionRepository.findAll()).thenReturn(List.of(
                question(2, "B Category"),
                question(1, "A Category"),
                question(3, "B Category")
        ));

        List<GrammarTopicResponse> topics = grammarService.getTopics("learner");

        assertThat(topics).extracting(GrammarTopicResponse::id)
                .containsExactly("A Category", "B Category");
        assertThat(topics.get(0).progress()).isZero();
        assertThat(topics.get(1).progress()).isEqualTo(50);
        assertThat(topics.get(1).examples()).hasSize(2);
    }

    @Test
    void getReviewQuestionsDelegatesToReviewServiceForCurrentUser() {
        AppUser user = user(7L, "learner");
        when(userRepository.findByUsername("learner")).thenReturn(Optional.of(user));
        when(reviewService.getDueGrammar(7L)).thenReturn(List.of(GrammarPracticeQuestionResponse.from(question(1, "Tense"))));

        List<GrammarPracticeQuestionResponse> response = grammarService.getReviewQuestions("learner");

        assertThat(response).extracting(GrammarPracticeQuestionResponse::id).containsExactly(1);
    }

    @Test
    void submitPracticeResultAtomicallyUpsertsGrammarbookAndRecordsProgress() {
        AppUser user = user(7L, "learner");
        when(userRepository.findByUsername("learner")).thenReturn(Optional.of(user));
        when(grammarQuestionRepository.existsById(1)).thenReturn(true);

        grammarService.submitPracticeResult("learner", new GrammarPracticeResultRequest(1, true));

        verify(userGrammarbookRepository).upsertPracticeResult(7L, 1, true);
        verify(userGrammarbookRepository, never()).findByUserIdAndGrammarQuestionId(7L, 1);
        verify(learningProgressOutboxService).enqueueGrammarPractice(7L, 1);
    }

    @Test
    void submitPracticeResultRejectsMissingQuestion() {
        AppUser user = user(7L, "learner");
        when(userRepository.findByUsername("learner")).thenReturn(Optional.of(user));
        when(grammarQuestionRepository.existsById(1)).thenReturn(false);

        assertThatThrownBy(() -> grammarService.submitPracticeResult(
                "learner",
                new GrammarPracticeResultRequest(1, true)
        ))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Grammar question was not found.");

        verify(userGrammarbookRepository, never()).upsertPracticeResult(any(), anyInt(), anyBoolean());
        verify(learningProgressOutboxService, never()).enqueueGrammarPractice(any(), anyInt());
    }

    @Test
    void submitRatingDelegatesToReviewService() {
        AppUser user = user(7L, "learner");
        when(userRepository.findByUsername("learner")).thenReturn(Optional.of(user));
        when(grammarQuestionRepository.existsById(1)).thenReturn(true);

        grammarService.submitRating("learner", new GrammarRatingRequest(1, 4));

        verify(reviewService).submitValidatedGrammarRating(7L, 1, 4);
    }

    @Test
    void toggleFavoriteFlipsExistingGrammarbookRow() {
        AppUser user = user(7L, "learner");
        UserGrammarbook row = grammarbook(7L, 1, false, false);
        when(userRepository.findByUsername("learner")).thenReturn(Optional.of(user));
        when(grammarQuestionRepository.existsById(1)).thenReturn(true);
        when(userGrammarbookRepository.findByUserIdAndGrammarQuestionId(7L, 1)).thenReturn(Optional.of(row));
        when(userGrammarbookRepository.save(row)).thenReturn(row);

        GrammarFavoriteResponse response = grammarService.toggleFavorite("learner", new GrammarFavoriteRequest(1));

        assertThat(response.favorited()).isTrue();
        verify(userGrammarbookRepository).save(row);
    }

    @Test
    void getNotebookQuestionsCombinesNotebookRowsWithQuestions() {
        AppUser user = user(7L, "learner");
        UserGrammarbook row = grammarbook(7L, 1, true, true);
        when(userRepository.findByUsername("learner")).thenReturn(Optional.of(user));
        when(userGrammarbookRepository.findNotebookRowsByUserId(7L)).thenReturn(List.of(row));
        when(grammarQuestionRepository.findAllById(List.of(1))).thenReturn(List.of(question(1, "Tense")));

        var questions = grammarService.getNotebookQuestions("learner");

        assertThat(questions).hasSize(1);
        assertThat(questions.get(0).wrong()).isTrue();
        assertThat(questions.get(0).favorited()).isTrue();
    }

    @Test
    void getNotebookQuestionsSkipsRowsWhoseQuestionNoLongerExists() {
        AppUser user = user(7L, "learner");
        UserGrammarbook missing = grammarbook(7L, 99, true, false);
        when(userRepository.findByUsername("learner")).thenReturn(Optional.of(user));
        when(userGrammarbookRepository.findNotebookRowsByUserId(7L)).thenReturn(List.of(missing));
        when(grammarQuestionRepository.findAllById(List.of(99))).thenReturn(List.of());

        var questions = grammarService.getNotebookQuestions("learner");

        assertThat(questions).isEmpty();
    }

    @Test
    void getProgressDelegatesToLearningPlanService() {
        when(learningPlanService.getGrammarProgress("learner"))
                .thenReturn(new DailyPracticeProgressResponse(2, 5, 3, false));

        DailyPracticeProgressResponse response = grammarService.getProgress("learner");

        assertThat(response.completed()).isEqualTo(2);
        assertThat(response.total()).isEqualTo(5);
    }

    @Test
    void toggleFavoriteRequiresAuthentication() {
        assertThatThrownBy(() -> grammarService.toggleFavorite(null, new GrammarFavoriteRequest(1)))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessage("Authentication is required.");
    }

    @Test
    void submitRatingRejectsMissingQuestion() {
        AppUser user = user(7L, "learner");
        when(userRepository.findByUsername("learner")).thenReturn(Optional.of(user));
        when(grammarQuestionRepository.existsById(1)).thenReturn(false);

        assertThatThrownBy(() -> grammarService.submitRating("learner", new GrammarRatingRequest(1, 3)))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Grammar question was not found.");

        verify(reviewService, never()).submitValidatedGrammarRating(any(), anyInt(), anyInt());
    }

    @Test
    void getPracticeQuestionsRejectsUnknownUser() {
        when(userRepository.findByUsername("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> grammarService.getPracticeQuestions("missing", "Tense"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Current user was not found.");
    }

    @Test
    void askTutorBuildsAuthoritativeQuestionContextAndLimitsRelatedMistakes() {
        AppUser user = user(7L, "learner");
        GrammarQuestion current = question(1, "Tense");
        List<GrammarQuestion> related = List.of(question(2, "Tense"), question(3, "Tense"));
        GrammarTutorRequest request = new GrammarTutorRequest(
                1,
                "B",
                "为什么不能选 B？",
                List.of(new GrammarTutorMessage("user", "我看不懂"))
        );
        when(userRepository.findByUsername("learner")).thenReturn(Optional.of(user));
        when(grammarQuestionRepository.findById(1)).thenReturn(Optional.of(current));
        when(userGrammarbookRepository.findRecentIncorrectQuestionsByCategory(
                eq(7L), eq("Tense"), eq(1), any(Pageable.class)
        )).thenReturn(related);
        when(grammarTutorAgentClient.explain(current, "B", request.history(), request.message(), related))
                .thenReturn("因为这里考查时态。" );

        GrammarTutorResponse response = grammarService.askTutor("learner", request);

        assertThat(response.reply()).isEqualTo("因为这里考查时态。" );
        assertThat(response.relatedMistakeCount()).isEqualTo(2);
        verify(grammarTutorAgentClient).explain(current, "B", request.history(), request.message(), related);
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

    private static GrammarQuestion question(Integer id, String category) {
        GrammarQuestion question = new GrammarQuestion();
        question.setId(id);
        question.setGrammarCategory(category);
        question.setQuestionText("Choose the answer.");
        question.setOptionA("A");
        question.setOptionB("B");
        question.setOptionC("C");
        question.setOptionD("D");
        question.setAnswer("A");
        question.setExplanation("Because.");
        return question;
    }

    private static UserWordProgress progress(
            String questionId,
            Integer reps,
            Integer state,
            Instant due,
            Double stability
    ) {
        UserWordProgress progress = new UserWordProgress();
        progress.setUserId(7L);
        progress.setQuestionType("grammar");
        progress.setQuestionId(questionId);
        progress.setReps(reps);
        progress.setState(state);
        progress.setDue(due);
        progress.setLastReview(Instant.now().minusSeconds(86_400));
        progress.setUpdatedAt(Instant.now().minusSeconds(86_400));
        progress.setStability(stability);
        return progress;
    }

    private static UserGrammarbook grammarbook(Long userId, Integer questionId, boolean incorrect, boolean favorited) {
        UserGrammarbook grammarbook = new UserGrammarbook();
        grammarbook.setUserId(userId);
        grammarbook.setGrammarQuestionId(questionId);
        grammarbook.setIncorrect(incorrect);
        grammarbook.setFavorited(favorited);
        return grammarbook;
    }
}
