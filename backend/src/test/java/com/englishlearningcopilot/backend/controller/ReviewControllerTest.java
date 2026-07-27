package com.englishlearningcopilot.backend.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.englishlearningcopilot.backend.dto.DueVocabularyCard;
import com.englishlearningcopilot.backend.exception.GlobalExceptionHandler;
import com.englishlearningcopilot.backend.service.ReviewService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@ExtendWith(MockitoExtension.class)
class ReviewControllerTest {

    @Mock
    private ReviewService reviewService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(new ReviewController(reviewService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(validator)
                .build();
    }

    @Test
    void getDueVocabularyResolvesUserIdFromPrincipal() throws Exception {
        when(reviewService.getUserIdByUsername("learner")).thenReturn(7L);
        when(reviewService.getDueVocabulary(7L)).thenReturn(List.of(card()));

        mockMvc.perform(get("/api/vocabulary/review-vocabulary").principal(() -> "learner"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(10))
                .andExpect(jsonPath("$[0].word").value("accept"));

        verify(reviewService).getDueVocabulary(7L);
    }

    @Test
    void submitRatingResolvesUserAndReturnsOk() throws Exception {
        when(reviewService.getUserIdByUsername("learner")).thenReturn(7L);

        mockMvc.perform(post("/api/vocabulary/review")
                        .principal(() -> "learner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "questionId": "10",
                                  "rating": 3
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));

        verify(reviewService).submitRating(7L, "10", 3);
    }

    @Test
    void submitRatingRejectsInvalidRating() throws Exception {
        mockMvc.perform(post("/api/vocabulary/review")
                        .principal(() -> "learner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "questionId": "",
                                  "rating": 5
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.questionId").exists())
                .andExpect(jsonPath("$.fieldErrors.rating").exists());
    }

    private static DueVocabularyCard card() {
        return new DueVocabularyCard(
                10L,
                "accept",
                "/ak-sept/",
                "receive willingly",
                "接受",
                "vt. 接受",
                "3",
                "1",
                "cet4",
                "1000",
                "2000",
                "",
                "uk.mp3",
                "us.mp3",
                List.of("接受", "拒绝"),
                List.of("accept", "reject")
        );
    }
}
