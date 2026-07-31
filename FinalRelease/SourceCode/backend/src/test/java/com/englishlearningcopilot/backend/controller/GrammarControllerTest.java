package com.englishlearningcopilot.backend.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.englishlearningcopilot.backend.dto.DailyPracticeProgressResponse;
import com.englishlearningcopilot.backend.dto.GrammarFavoriteRequest;
import com.englishlearningcopilot.backend.dto.GrammarFavoriteResponse;
import com.englishlearningcopilot.backend.dto.GrammarNotebookQuestionResponse;
import com.englishlearningcopilot.backend.dto.GrammarOverviewResponse;
import com.englishlearningcopilot.backend.dto.GrammarPracticeQuestionResponse;
import com.englishlearningcopilot.backend.dto.GrammarPracticeResultRequest;
import com.englishlearningcopilot.backend.dto.GrammarRatingRequest;
import com.englishlearningcopilot.backend.dto.GrammarTopicResponse;
import com.englishlearningcopilot.backend.dto.GrammarTutorResponse;
import com.englishlearningcopilot.backend.exception.GlobalExceptionHandler;
import com.englishlearningcopilot.backend.service.GrammarService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@ExtendWith(MockitoExtension.class)
class GrammarControllerTest {

    @Mock
    private GrammarService grammarService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(new GrammarController(grammarService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(validator)
                .build();
    }

    @Test
    void getOverviewPassesPrincipalNameToService() throws Exception {
        when(grammarService.getOverview("learner"))
                .thenReturn(new GrammarOverviewResponse(80, List.of()));

        mockMvc.perform(get("/api/grammar/overview").principal(() -> "learner"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.masteryRate").value(80));

        verify(grammarService).getOverview("learner");
    }

    @Test
    void getOverviewPassesNullWhenPrincipalMissing() throws Exception {
        when(grammarService.getOverview(null))
                .thenReturn(new GrammarOverviewResponse(0, List.of()));

        mockMvc.perform(get("/api/grammar/overview"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.masteryRate").value(0));

        verify(grammarService).getOverview(null);
    }

    @Test
    void getTopicsReturnsTopicProgress() throws Exception {
        when(grammarService.getTopics("learner"))
                .thenReturn(List.of(new GrammarTopicResponse("Tense", "Tense", "summary", List.of("example"), 50, "2 questions")));

        mockMvc.perform(get("/api/grammar/topics").principal(() -> "learner"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("Tense"))
                .andExpect(jsonPath("$[0].progress").value(50));
    }

    @Test
    void getTopicsPassesNullWhenPrincipalMissing() throws Exception {
        when(grammarService.getTopics(null)).thenReturn(List.of());

        mockMvc.perform(get("/api/grammar/topics"))
                .andExpect(status().isOk());

        verify(grammarService).getTopics(null);
    }

    @Test
    void getPracticeQuestionsDelegatesCategoryAndPrincipal() throws Exception {
        when(grammarService.getPracticeQuestions("learner", "Tense"))
                .thenReturn(List.of(question(1, "Tense")));

        mockMvc.perform(get("/api/grammar/practice-questions")
                        .principal(() -> "learner")
                        .param("category", "Tense"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].grammar_category").value("Tense"));

        verify(grammarService).getPracticeQuestions("learner", "Tense");
    }

    @Test
    void submitPracticeResultValidatesBodyAndDelegates() throws Exception {
        mockMvc.perform(post("/api/grammar/practice-results")
                        .principal(() -> "learner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "grammarQuestionId": 1,
                                  "incorrect": true
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Grammar practice result received."));

        ArgumentCaptor<GrammarPracticeResultRequest> captor =
                ArgumentCaptor.forClass(GrammarPracticeResultRequest.class);
        verify(grammarService).submitPracticeResult(org.mockito.Mockito.eq("learner"), captor.capture());
        org.assertj.core.api.Assertions.assertThat(captor.getValue().incorrect()).isTrue();
    }

    @Test
    void submitRatingRejectsOutOfRangeScore() throws Exception {
        mockMvc.perform(post("/api/grammar/practice-ratings")
                        .principal(() -> "learner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "grammarQuestionId": 1,
                                  "score": 5
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.score").exists());
    }

    @Test
    void toggleFavoriteReturnsFavoriteState() throws Exception {
        when(grammarService.toggleFavorite(any(String.class), any(GrammarFavoriteRequest.class)))
                .thenReturn(new GrammarFavoriteResponse(1, true));

        mockMvc.perform(post("/api/grammar/notebook-favorites")
                        .principal(() -> "learner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "grammarQuestionId": 1
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.grammarQuestionId").value(1))
                .andExpect(jsonPath("$.favorited").value(true));
    }

    @Test
    void askTutorValidatesAndReturnsReply() throws Exception {
        when(grammarService.askTutor(org.mockito.Mockito.eq("learner"), any()))
                .thenReturn(new GrammarTutorResponse("因为这里需要定语从句。", 2));

        mockMvc.perform(post("/api/grammar/tutor/messages")
                        .principal(() -> "learner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "grammarQuestionId": 1,
                                  "selectedAnswer": "B",
                                  "message": "为什么？",
                                  "history": []
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reply").value("因为这里需要定语从句。"))
                .andExpect(jsonPath("$.relatedMistakeCount").value(2));
    }

    @Test
    void askTutorRejectsTooManyHistoryMessages() throws Exception {
        mockMvc.perform(post("/api/grammar/tutor/messages")
                        .principal(() -> "learner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "grammarQuestionId": 1,
                                  "selectedAnswer": "A",
                                  "message": "请解释",
                                  "history": [
                                    {"role":"user","content":"1"},
                                    {"role":"assistant","content":"2"},
                                    {"role":"user","content":"3"},
                                    {"role":"assistant","content":"4"},
                                    {"role":"user","content":"5"},
                                    {"role":"assistant","content":"6"},
                                    {"role":"user","content":"7"},
                                    {"role":"assistant","content":"8"},
                                    {"role":"user","content":"9"}
                                  ]
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.history").exists());
    }

    @Test
    void getProgressReturnsDailyProgress() throws Exception {
        when(grammarService.getProgress("learner"))
                .thenReturn(new DailyPracticeProgressResponse(1, 5, 4, false));

        mockMvc.perform(get("/api/grammar/progress").principal(() -> "learner"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.completed").value(1))
                .andExpect(jsonPath("$.total").value(5));
    }

    @Test
    void getReviewQuestionsUsesPrincipalName() throws Exception {
        when(grammarService.getReviewQuestions("learner"))
                .thenReturn(List.of(question(2, "Clause")));

        mockMvc.perform(get("/api/grammar/review-grammar").principal(() -> "learner"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(2));

        verify(grammarService).getReviewQuestions("learner");
    }

    @Test
    void getNotebookQuestionsUsesPrincipalName() throws Exception {
        when(grammarService.getNotebookQuestions("learner"))
                .thenReturn(List.of(new GrammarNotebookQuestionResponse(
                        3,
                        "Pick one.",
                        List.of("A", "B"),
                        "A",
                        "Tense",
                        "Because.",
                        true,
                        true
                )));

        mockMvc.perform(get("/api/grammar/notebook-questions").principal(() -> "learner"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(3))
                .andExpect(jsonPath("$[0].favorited").value(true));

        verify(grammarService).getNotebookQuestions("learner");
    }

    @Test
    void getNotebookQuestionsPassesNullWhenPrincipalMissing() throws Exception {
        when(grammarService.getNotebookQuestions(null)).thenReturn(List.of());

        mockMvc.perform(get("/api/grammar/notebook-questions"))
                .andExpect(status().isOk());

        verify(grammarService).getNotebookQuestions(null);
    }

    @Test
    void submitRatingValidatesBodyAndDelegates() throws Exception {
        mockMvc.perform(post("/api/grammar/practice-ratings")
                        .principal(() -> "learner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "grammarQuestionId": 1,
                                  "score": 3
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Grammar rating received."));

        verify(grammarService).submitRating(org.mockito.Mockito.eq("learner"), any(GrammarRatingRequest.class));
    }

    private static GrammarPracticeQuestionResponse question(Integer id, String category) {
        return new GrammarPracticeQuestionResponse(
                id,
                "Choose the answer.",
                List.of("A", "B", "C", "D"),
                "A",
                category,
                "Because."
        );
    }
}
