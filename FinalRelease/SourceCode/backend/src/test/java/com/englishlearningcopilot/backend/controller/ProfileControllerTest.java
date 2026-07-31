package com.englishlearningcopilot.backend.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.englishlearningcopilot.backend.dto.DailyLearningStatusResponse;
import com.englishlearningcopilot.backend.dto.DailyPracticeProgressResponse;
import com.englishlearningcopilot.backend.dto.LearningPlanRequest;
import com.englishlearningcopilot.backend.dto.LearningPlanResponse;
import com.englishlearningcopilot.backend.dto.ProfileSnapshotResponse;
import com.englishlearningcopilot.backend.exception.GlobalExceptionHandler;
import com.englishlearningcopilot.backend.service.LearningPlanService;
import java.time.LocalDate;
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
class ProfileControllerTest {

    @Mock
    private LearningPlanService learningPlanService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(new ProfileController(learningPlanService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(validator)
                .build();
    }

    @Test
    void getSnapshotUsesPrincipalName() throws Exception {
        when(learningPlanService.getProfileSnapshot("learner")).thenReturn(snapshot());

        mockMvc.perform(get("/api/profile/snapshot").principal(() -> "learner"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.learnerName").value("Learner"))
                .andExpect(jsonPath("$.feedback.totalScore").value(86));

        verify(learningPlanService).getProfileSnapshot("learner");
    }

    @Test
    void getLearningPlanUsesPrincipalName() throws Exception {
        when(learningPlanService.getLearningPlan("learner"))
                .thenReturn(new LearningPlanResponse(20, 5, true));

        mockMvc.perform(get("/api/profile/learning-plan").principal(() -> "learner"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dailyVocabularyGoal").value(20))
                .andExpect(jsonPath("$.enabled").value(true));
    }

    @Test
    void updateLearningPlanValidatesAndDelegates() throws Exception {
        when(learningPlanService.updateLearningPlan(eq("learner"), any(LearningPlanRequest.class)))
                .thenReturn(new LearningPlanResponse(30, 8, true));

        mockMvc.perform(post("/api/profile/learning-plan")
                        .principal(() -> "learner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "dailyVocabularyGoal": 30,
                                  "dailyGrammarGoal": 8
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dailyVocabularyGoal").value(30))
                .andExpect(jsonPath("$.dailyGrammarGoal").value(8));

        ArgumentCaptor<LearningPlanRequest> captor = ArgumentCaptor.forClass(LearningPlanRequest.class);
        verify(learningPlanService).updateLearningPlan(eq("learner"), captor.capture());
        org.assertj.core.api.Assertions.assertThat(captor.getValue().dailyVocabularyGoal()).isEqualTo(30);
    }

    @Test
    void updateLearningPlanRejectsInvalidGoal() throws Exception {
        mockMvc.perform(post("/api/profile/learning-plan")
                        .principal(() -> "learner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "dailyVocabularyGoal": 201,
                                  "dailyGrammarGoal": 8
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.dailyVocabularyGoal").exists());
    }

    @Test
    void getDailyStatusUsesPrincipalName() throws Exception {
        when(learningPlanService.getDailyStatus("learner"))
                .thenReturn(new DailyLearningStatusResponse(
                        LocalDate.of(2026, 7, 27),
                        new DailyPracticeProgressResponse(1, 2, 1, false),
                        new DailyPracticeProgressResponse(2, 2, 0, true),
                        false,
                        3
                ));

        mockMvc.perform(get("/api/profile/daily-status").principal(() -> "learner"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.date[0]").value(2026))
                .andExpect(jsonPath("$.date[1]").value(7))
                .andExpect(jsonPath("$.date[2]").value(27))
                .andExpect(jsonPath("$.streakDays").value(3));
    }

    private static ProfileSnapshotResponse snapshot() {
        DailyPracticeProgressResponse vocabulary = new DailyPracticeProgressResponse(1, 3, 2, false);
        DailyPracticeProgressResponse grammar = new DailyPracticeProgressResponse(2, 2, 0, true);
        return new ProfileSnapshotResponse(
                "Learner",
                "B1",
                "3 days",
                new ProfileSnapshotResponse.FeedbackSummary(
                        "最近一次口语反馈",
                        null,
                        List.of(),
                        List.of(),
                        "Airport Check-in",
                        "2026-07-27 14:00",
                        86,
                        88,
                        85,
                        87,
                        "128 WPM",
                        "无"
                ),
                new ProfileSnapshotResponse.ProfileDailyPlan(
                        true,
                        "+12%",
                        20,
                        5,
                        vocabulary,
                        grammar,
                        false,
                        List.of(),
                        List.of()
                )
        );
    }
}
