package com.englishlearningcopilot.backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.englishlearningcopilot.backend.entity.AppUser;
import com.englishlearningcopilot.backend.entity.GrammarQuestion;
import com.englishlearningcopilot.backend.entity.UserRole;
import com.englishlearningcopilot.backend.entity.UserWordProgress;
import com.englishlearningcopilot.backend.entity.Vocabulary;
import com.englishlearningcopilot.backend.exception.ResourceNotFoundException;
import com.englishlearningcopilot.backend.fsrs.FSRS;
import com.englishlearningcopilot.backend.repository.GrammarQuestionRepository;
import com.englishlearningcopilot.backend.repository.UserRepository;
import com.englishlearningcopilot.backend.repository.UserWordProgressRepository;
import com.englishlearningcopilot.backend.repository.VocabularyRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private UserWordProgressRepository progressRepository;

    @Mock
    private VocabularyRepository vocabularyRepository;

    @Mock
    private GrammarQuestionRepository grammarQuestionRepository;

    @Mock
    private UserRepository userRepository;

    private ReviewService reviewService;

    @BeforeEach
    void setUp() {
        reviewService = new ReviewService(
                progressRepository,
                vocabularyRepository,
                grammarQuestionRepository,
                userRepository
        );
    }

    @Test
    void submitRatingRejectsInvalidVocabularyQuestionId() {
        assertThatThrownBy(() -> reviewService.submitRating(7L, "not-number", 3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid vocabulary questionId: not-number");
    }

    @Test
    void submitRatingRejectsMissingVocabularyBeforeSavingProgress() {
        when(vocabularyRepository.existsById(10L)).thenReturn(false);

        assertThatThrownBy(() -> reviewService.submitRating(7L, "10", 3))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Vocabulary word was not found.");
    }

    @Test
    void submitRatingAcceptsExistingVocabulary() {
        when(vocabularyRepository.existsById(10L)).thenReturn(true);
        when(progressRepository.findByUserIdAndQuestionIdAndQuestionType(7L, "10", "vocabulary"))
                .thenReturn(Optional.empty());
        when(progressRepository.save(any(UserWordProgress.class))).thenAnswer(invocation -> invocation.getArgument(0));

        reviewService.submitRating(7L, "10", 3);

        verify(progressRepository).save(any(UserWordProgress.class));
    }

    @Test
    void submitValidatedVocabularyRatingCreatesProgressWhenMissing() {
        when(progressRepository.findByUserIdAndQuestionIdAndQuestionType(7L, "10", "vocabulary"))
                .thenReturn(Optional.empty());
        when(progressRepository.save(any(UserWordProgress.class))).thenAnswer(invocation -> invocation.getArgument(0));

        reviewService.submitValidatedVocabularyRating(7L, "10", 4);

        verify(progressRepository).save(any(UserWordProgress.class));
    }

    @Test
    void submitValidatedVocabularyRatingUsesDefaultsForPartialExistingProgress() {
        UserWordProgress progress = new UserWordProgress();
        progress.setUserId(7L);
        progress.setQuestionId("10");
        progress.setQuestionType("vocabulary");
        when(progressRepository.findByUserIdAndQuestionIdAndQuestionType(7L, "10", "vocabulary"))
                .thenReturn(Optional.of(progress));
        when(progressRepository.save(progress)).thenReturn(progress);

        reviewService.submitValidatedVocabularyRating(7L, "10", 2);

        assertThat(progress.getState()).isEqualTo(1);
        assertThat(progress.getDifficulty()).isNotNull();
        assertThat(progress.getStability()).isNotNull();
        assertThat(progress.getDue()).isNotNull();
        assertThat(progress.getLastReview()).isNotNull();
    }

    @Test
    void submitGrammarRatingRejectsMissingQuestion() {
        when(grammarQuestionRepository.existsById(1)).thenReturn(false);

        assertThatThrownBy(() -> reviewService.submitGrammarRating(7L, 1, 3))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Grammar question was not found.");
    }

    @Test
    void submitGrammarRatingAcceptsExistingQuestion() {
        when(grammarQuestionRepository.existsById(1)).thenReturn(true);
        when(progressRepository.findByUserIdAndQuestionIdAndQuestionType(7L, "1", "grammar"))
                .thenReturn(Optional.empty());
        when(progressRepository.save(any(UserWordProgress.class))).thenAnswer(invocation -> invocation.getArgument(0));

        reviewService.submitGrammarRating(7L, 1, 3);

        verify(progressRepository).save(any(UserWordProgress.class));
    }

    @Test
    void submitValidatedGrammarRatingUpdatesExistingReviewCard() {
        UserWordProgress progress = existingReviewProgress("1", "grammar");
        when(progressRepository.findByUserIdAndQuestionIdAndQuestionType(7L, "1", "grammar"))
                .thenReturn(Optional.of(progress));
        when(progressRepository.save(progress)).thenReturn(progress);

        reviewService.submitValidatedGrammarRating(7L, 1, 1);

        assertThat(progress.getLapses()).isGreaterThanOrEqualTo(1);
        assertThat(progress.getState()).isEqualTo(1);
        verify(progressRepository).save(progress);
    }

    @Test
    void applyCardStateCanPersistNewState() {
        UserWordProgress progress = existingReviewProgress("1", "vocabulary");
        FSRS.CardState card = new FSRS.CardState();
        card.difficulty = 2.5;
        card.stability = 2.5;
        card.interval = 0;
        card.reps = 0;
        card.lapses = 0;
        card.state = FSRS.CardState.State.New;
        card.due = Instant.now();
        card.lastReview = Instant.now();

        ReflectionTestUtils.invokeMethod(reviewService, "applyCardState", progress, card);

        assertThat(progress.getState()).isZero();
    }

    @Test
    void getDueVocabularySkipsRowsWhoseVocabularyNoLongerExists() {
        UserWordProgress valid = dueProgress("10", "vocabulary");
        UserWordProgress missing = dueProgress("11", "vocabulary");
        when(progressRepository.findByUserIdAndQuestionTypeAndDueBeforeOrderByDueAsc(
                eq(7L), eq("vocabulary"), any(Instant.class)
        )).thenReturn(List.of(valid, missing));
        when(vocabularyRepository.findById(10L)).thenReturn(Optional.of(vocabulary(10L)));
        when(vocabularyRepository.findById(11L)).thenReturn(Optional.empty());

        var due = reviewService.getDueVocabulary(7L);

        assertThat(due).hasSize(1);
        assertThat(due.get(0).word()).isEqualTo("accept");
    }

    @Test
    void getDueGrammarRejectsInvalidStoredQuestionId() {
        when(progressRepository.findByUserIdAndQuestionTypeAndDueBeforeOrderByDueAsc(
                eq(7L), eq("grammar"), any(Instant.class)
        )).thenReturn(List.of(dueProgress("bad", "grammar")));

        assertThatThrownBy(() -> reviewService.getDueGrammar(7L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid grammar questionId: bad");
    }

    @Test
    void getDueGrammarMapsExistingQuestions() {
        GrammarQuestion question = grammarQuestion(1);
        when(progressRepository.findByUserIdAndQuestionTypeAndDueBeforeOrderByDueAsc(
                eq(7L), eq("grammar"), any(Instant.class)
        )).thenReturn(List.of(dueProgress("1", "grammar")));
        when(grammarQuestionRepository.findById(1)).thenReturn(Optional.of(question));

        var due = reviewService.getDueGrammar(7L);

        assertThat(due).hasSize(1);
        assertThat(due.get(0).id()).isEqualTo(1);
    }

    @Test
    void getUserIdByUsernameReturnsCurrentUserId() {
        when(userRepository.findByUsername("learner")).thenReturn(Optional.of(user(7L, "learner")));

        assertThat(reviewService.getUserIdByUsername("learner")).isEqualTo(7L);
    }

    @Test
    void getUserIdByUsernameRejectsMissingUser() {
        when(userRepository.findByUsername("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reviewService.getUserIdByUsername("missing"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Current user was not found.");
    }

    private static UserWordProgress existingReviewProgress(String questionId, String questionType) {
        UserWordProgress progress = dueProgress(questionId, questionType);
        progress.setDifficulty(5.0);
        progress.setStability(4.0);
        progress.setInterval(4);
        progress.setReps(3);
        progress.setLapses(0);
        progress.setState(1);
        progress.setLastReview(Instant.now().minusSeconds(4 * 86_400L));
        return progress;
    }

    private static UserWordProgress dueProgress(String questionId, String questionType) {
        UserWordProgress progress = new UserWordProgress();
        progress.setUserId(7L);
        progress.setQuestionId(questionId);
        progress.setQuestionType(questionType);
        progress.setDue(Instant.now().minusSeconds(60));
        return progress;
    }

    private static Vocabulary vocabulary(Long id) {
        Vocabulary vocabulary = new Vocabulary();
        vocabulary.setId(id);
        vocabulary.setWord("accept");
        vocabulary.setBriefTranslation("accept");
        vocabulary.setDefinition("definition");
        return vocabulary;
    }

    private static GrammarQuestion grammarQuestion(Integer id) {
        GrammarQuestion question = new GrammarQuestion();
        question.setId(id);
        question.setGrammarCategory("Tense");
        question.setQuestionText("Choose.");
        question.setOptionA("A");
        question.setOptionB("B");
        question.setOptionC("C");
        question.setOptionD("D");
        question.setAnswer("A");
        question.setExplanation("Because.");
        return question;
    }

    private static AppUser user(Long id, String username) {
        AppUser user = new AppUser();
        ReflectionTestUtils.setField(user, "id", id);
        user.setUsername(username);
        user.setEmail(username + "@example.com");
        user.setDisplayName("Learner");
        user.setPasswordHash("hash");
        user.setRole(UserRole.USER);
        user.setEnabled(true);
        return user;
    }
}
