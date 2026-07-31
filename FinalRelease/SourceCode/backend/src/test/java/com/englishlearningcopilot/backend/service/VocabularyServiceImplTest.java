package com.englishlearningcopilot.backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.englishlearningcopilot.backend.dto.DailyPracticeProgressResponse;
import com.englishlearningcopilot.backend.dto.VocabularyFavoriteRequest;
import com.englishlearningcopilot.backend.dto.VocabularyFavoriteResponse;
import com.englishlearningcopilot.backend.dto.VocabularyPracticeWordResponse;
import com.englishlearningcopilot.backend.dto.VocabularyRatingRequest;
import com.englishlearningcopilot.backend.dto.VocabularyWordbookWordResponse;
import com.englishlearningcopilot.backend.entity.AppUser;
import com.englishlearningcopilot.backend.entity.UserRole;
import com.englishlearningcopilot.backend.entity.UserWordProgress;
import com.englishlearningcopilot.backend.entity.UserWordbook;
import com.englishlearningcopilot.backend.entity.Vocabulary;
import com.englishlearningcopilot.backend.exception.ResourceNotFoundException;
import com.englishlearningcopilot.backend.repository.UserRepository;
import com.englishlearningcopilot.backend.repository.UserWordProgressRepository;
import com.englishlearningcopilot.backend.repository.UserWordbookRepository;
import com.englishlearningcopilot.backend.repository.VocabularyRepository;
import com.englishlearningcopilot.backend.service.impl.VocabularyServiceImpl;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class VocabularyServiceImplTest {

    @Mock
    private VocabularyRepository vocabularyRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserWordProgressRepository userWordProgressRepository;

    @Mock
    private UserWordbookRepository userWordbookRepository;

    @Mock
    private ReviewService reviewService;

    @Mock
    private LearningPlanService learningPlanService;

    @Mock
    private LearningProgressOutboxService learningProgressOutboxService;

    @InjectMocks
    private VocabularyServiceImpl vocabularyService;

    @Test
    void getPracticeWordsForAnonymousUserUsesLevelTagsWithoutUserLookup() {
        when(vocabularyRepository.findRandomPracticeWordsByTags(
                eq("zk"), eq("gk"), any(String.class), any(String.class), any(Pageable.class)
        )).thenReturn(List.of(vocabulary(10L, "accept", "zk gk")));

        List<VocabularyPracticeWordResponse> words = vocabularyService.getPracticeWords(null, "starter");

        assertThat(words).extracting(VocabularyPracticeWordResponse::word).containsExactly("accept");
        verify(userRepository, never()).findByUsername(any());
    }

    @Test
    void getPracticeWordsDefaultsNullLevelToStarter() {
        when(vocabularyRepository.findRandomPracticeWordsByTags(
                eq("zk"), eq("gk"), any(String.class), any(String.class), any(Pageable.class)
        )).thenReturn(List.of(vocabulary(10L, "accept", "zk gk")));

        List<VocabularyPracticeWordResponse> words = vocabularyService.getPracticeWords(null, null);

        assertThat(words).extracting(VocabularyPracticeWordResponse::word).containsExactly("accept");
    }

    @Test
    void getPracticeWordsForLoggedInUserExcludesLearnedWords() {
        AppUser user = user(7L, "learner");
        when(userRepository.findByUsername("learner")).thenReturn(Optional.of(user));
        when(vocabularyRepository.findRandomUnlearnedPracticeWordsByTags(
                eq(7L), eq("cet4"), any(String.class), any(String.class), any(String.class), any(Pageable.class)
        )).thenReturn(List.of(vocabulary(10L, "accept", "cet4")));

        List<VocabularyPracticeWordResponse> words = vocabularyService.getPracticeWords("learner", "basic");

        assertThat(words).extracting(VocabularyPracticeWordResponse::id).containsExactly(10L);
    }

    @Test
    void getPracticeWordsRejectsUnsupportedLevel() {
        assertThatThrownBy(() -> vocabularyService.getPracticeWords(null, "unknown"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Unsupported vocabulary practice level: unknown");
    }

    @Test
    void getMemoryForAnonymousUserReturnsEmptyStats() {
        Map<String, Object> memory = vocabularyService.getMemory(null);

        assertThat(memory.get("retentionRate")).isEqualTo(0);
        assertThat(memory.get("stats").toString()).contains("0");
        verify(userWordProgressRepository, never()).findByUserIdAndQuestionType(any(), any());
    }

    @Test
    void getMemoryForUnknownUserReturnsEmptyStats() {
        when(userRepository.findByUsername("missing")).thenReturn(Optional.empty());

        Map<String, Object> memory = vocabularyService.getMemory("missing");

        assertThat(memory.get("retentionRate")).isEqualTo(0);
        assertThat(memory.get("stats").toString()).contains("0");
        verify(userWordProgressRepository, never()).findByUserIdAndQuestionType(any(), any());
    }

    @Test
    void getMemoryCountsOnlyReviewCardsAndDueItems() {
        AppUser user = user(7L, "learner");
        when(userRepository.findByUsername("learner")).thenReturn(Optional.of(user));
        when(userWordProgressRepository.findByUserIdAndQuestionType(7L, "vocabulary"))
                .thenReturn(List.of(
                        progress(1, Instant.now().minusSeconds(60), 2.5),
                        progress(1, Instant.now().plusSeconds(60), 5.0),
                        progress(1, null, 5.0),
                        progress(null, Instant.now().minusSeconds(60), 2.5),
                        progress(0, Instant.now().minusSeconds(60), 2.5)
                ));

        Map<String, Object> memory = vocabularyService.getMemory("learner");

        assertThat((Integer) memory.get("retentionRate")).isBetween(1, 100);
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> stats = (List<Map<String, Object>>) memory.get("stats");
        assertThat(stats)
                .extracting(stat -> stat.get("value"))
                .containsExactly("3 词", "1 词", "0 词");
    }

    @Test
    void getMemoryUsesUpdatedAtAndMinimumStabilityWhenReviewMetadataIsPartial() {
        AppUser user = user(7L, "learner");
        UserWordProgress progress = progress(1, Instant.now().minusSeconds(60), null);
        progress.setLastReview(null);
        progress.setUpdatedAt(null);
        when(userRepository.findByUsername("learner")).thenReturn(Optional.of(user));
        when(userWordProgressRepository.findByUserIdAndQuestionType(7L, "vocabulary"))
                .thenReturn(List.of(progress));

        Map<String, Object> memory = vocabularyService.getMemory("learner");

        assertThat((Integer) memory.get("retentionRate")).isBetween(1, 100);
        assertThat(memory.get("stats").toString()).contains("1");
    }

    @Test
    void getWordbookWordsCombinesWordbookRowsWithVocabularyDetails() {
        AppUser user = user(7L, "learner");
        UserWordbook wordbook = wordbook(7L, 10L, true);
        when(userRepository.findByUsername("learner")).thenReturn(Optional.of(user));
        when(userWordbookRepository.findByUserIdOrderByIdDesc(7L)).thenReturn(List.of(wordbook));
        when(vocabularyRepository.findAllById(List.of(10L)))
                .thenReturn(List.of(vocabulary(10L, "accept", "cet4")));

        List<VocabularyWordbookWordResponse> words = vocabularyService.getWordbookWords("learner");

        assertThat(words).hasSize(1);
        assertThat(words.get(0).word()).isEqualTo("accept");
        assertThat(words.get(0).favorited()).isTrue();
    }

    @Test
    void getWordbookWordsSkipsRowsWhoseVocabularyNoLongerExists() {
        AppUser user = user(7L, "learner");
        when(userRepository.findByUsername("learner")).thenReturn(Optional.of(user));
        when(userWordbookRepository.findByUserIdOrderByIdDesc(7L)).thenReturn(List.of(wordbook(7L, 99L, true)));
        when(vocabularyRepository.findAllById(List.of(99L))).thenReturn(List.of());

        List<VocabularyWordbookWordResponse> words = vocabularyService.getWordbookWords("learner");

        assertThat(words).isEmpty();
    }

    @Test
    void submitRatingAtomicallyRegistersWordbookAndUpdatesFsrs() {
        AppUser user = user(7L, "learner");
        when(userRepository.findByUsername("learner")).thenReturn(Optional.of(user));
        when(vocabularyRepository.existsById(10L)).thenReturn(true);

        vocabularyService.submitRating("learner", new VocabularyRatingRequest(10L, 3));

        verify(userWordbookRepository).insertIfAbsent(7L, 10L);
        verify(userWordbookRepository, never()).findByUserIdAndVocabularyId(7L, 10L);
        verify(reviewService).submitValidatedVocabularyRating(7L, "10", 3);
        verify(learningProgressOutboxService).enqueueVocabularyPractice(7L, 10L);
    }

    @Test
    void submitRatingUsesAtomicWordbookRegistrationForExistingWords() {
        AppUser user = user(7L, "learner");
        when(userRepository.findByUsername("learner")).thenReturn(Optional.of(user));
        when(vocabularyRepository.existsById(10L)).thenReturn(true);

        vocabularyService.submitRating("learner", new VocabularyRatingRequest(10L, 4));

        verify(userWordbookRepository).insertIfAbsent(7L, 10L);
        verify(reviewService).submitValidatedVocabularyRating(7L, "10", 4);
    }

    @Test
    void submitRatingRejectsMissingVocabulary() {
        AppUser user = user(7L, "learner");
        when(userRepository.findByUsername("learner")).thenReturn(Optional.of(user));
        when(vocabularyRepository.existsById(10L)).thenReturn(false);

        assertThatThrownBy(() -> vocabularyService.submitRating("learner", new VocabularyRatingRequest(10L, 3)))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Vocabulary word was not found.");

        verify(reviewService, never()).submitRating(any(), any(), anyInt());
        verify(reviewService, never()).submitValidatedVocabularyRating(any(), any(), anyInt());
        verify(userWordbookRepository, never()).insertIfAbsent(any(), any());
    }

    @Test
    void toggleFavoriteFlipsExistingWordbookFavoriteState() {
        AppUser user = user(7L, "learner");
        UserWordbook wordbook = wordbook(7L, 10L, false);
        when(userRepository.findByUsername("learner")).thenReturn(Optional.of(user));
        when(vocabularyRepository.existsById(10L)).thenReturn(true);
        when(userWordbookRepository.findByUserIdAndVocabularyId(7L, 10L)).thenReturn(Optional.of(wordbook));
        when(userWordbookRepository.save(wordbook)).thenReturn(wordbook);

        VocabularyFavoriteResponse response = vocabularyService.toggleFavorite(
                "learner",
                new VocabularyFavoriteRequest(10L)
        );

        assertThat(response.favorited()).isTrue();
        verify(userWordbookRepository).save(wordbook);
    }

    @Test
    void toggleFavoriteCanTurnOffExistingFavorite() {
        AppUser user = user(7L, "learner");
        UserWordbook wordbook = wordbook(7L, 10L, true);
        when(userRepository.findByUsername("learner")).thenReturn(Optional.of(user));
        when(vocabularyRepository.existsById(10L)).thenReturn(true);
        when(userWordbookRepository.findByUserIdAndVocabularyId(7L, 10L)).thenReturn(Optional.of(wordbook));
        when(userWordbookRepository.save(wordbook)).thenReturn(wordbook);

        VocabularyFavoriteResponse response = vocabularyService.toggleFavorite(
                "learner",
                new VocabularyFavoriteRequest(10L)
        );

        assertThat(response.favorited()).isFalse();
    }

    @Test
    void toggleFavoriteRejectsMissingVocabulary() {
        AppUser user = user(7L, "learner");
        when(userRepository.findByUsername("learner")).thenReturn(Optional.of(user));
        when(vocabularyRepository.existsById(10L)).thenReturn(false);

        assertThatThrownBy(() -> vocabularyService.toggleFavorite("learner", new VocabularyFavoriteRequest(10L)))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Vocabulary word was not found.");

        verify(userWordbookRepository, never()).save(any());
    }

    @Test
    void toggleFavoriteCreatesWordbookRowWhenMissing() {
        AppUser user = user(7L, "learner");
        when(userRepository.findByUsername("learner")).thenReturn(Optional.of(user));
        when(vocabularyRepository.existsById(10L)).thenReturn(true);
        when(userWordbookRepository.findByUserIdAndVocabularyId(7L, 10L)).thenReturn(Optional.empty());
        when(userWordbookRepository.save(any(UserWordbook.class))).thenAnswer(invocation -> invocation.getArgument(0));

        VocabularyFavoriteResponse response = vocabularyService.toggleFavorite(
                "learner",
                new VocabularyFavoriteRequest(10L)
        );

        assertThat(response.favorited()).isTrue();
    }

    @Test
    void getWordbookWordsRejectsUnknownUser() {
        when(userRepository.findByUsername("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> vocabularyService.getWordbookWords("missing"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Current user was not found.");
    }

    @Test
    void toggleFavoriteRequiresAuthentication() {
        assertThatThrownBy(() -> vocabularyService.toggleFavorite(null, new VocabularyFavoriteRequest(10L)))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessage("Authentication is required.");
    }

    @Test
    void getPracticeProgressDelegatesToLearningPlanService() {
        when(learningPlanService.getVocabularyProgress("learner"))
                .thenReturn(new DailyPracticeProgressResponse(3, 8, 5, false));

        DailyPracticeProgressResponse response = vocabularyService.getPracticeProgress("learner");

        assertThat(response.completed()).isEqualTo(3);
        assertThat(response.total()).isEqualTo(8);
        assertThat(response.remaining()).isEqualTo(5);
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

    private static Vocabulary vocabulary(Long id, String word, String tag) {
        Vocabulary vocabulary = new Vocabulary();
        vocabulary.setId(id);
        vocabulary.setWord(word);
        vocabulary.setPhonetic("/" + word + "/");
        vocabulary.setDefinition("definition");
        vocabulary.setBriefTranslation("释义");
        vocabulary.setTranslation("translation");
        vocabulary.setTag(tag);
        vocabulary.setUsAudio(word + ".mp3");
        vocabulary.setChineseOptions(List.of("释义", "干扰项"));
        vocabulary.setEnglishOptions(List.of(word, "other"));
        return vocabulary;
    }

    private static UserWordbook wordbook(Long userId, Long vocabularyId, boolean favorited) {
        UserWordbook wordbook = new UserWordbook();
        wordbook.setUserId(userId);
        wordbook.setVocabularyId(vocabularyId);
        wordbook.setFavorited(favorited);
        return wordbook;
    }

    private static UserWordProgress progress(Integer state, Instant due, Double stability) {
        UserWordProgress progress = new UserWordProgress();
        progress.setQuestionType("vocabulary");
        progress.setQuestionId("10");
        progress.setState(state);
        progress.setDue(due);
        progress.setLastReview(Instant.now().minusSeconds(86_400));
        progress.setUpdatedAt(Instant.now().minusSeconds(86_400));
        progress.setStability(stability);
        return progress;
    }
}
