package com.englishlearningcopilot.backend.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.englishlearningcopilot.backend.dto.DailyPracticeProgressResponse;
import com.englishlearningcopilot.backend.dto.DashboardCommunityLearningTrendsResponse;
import com.englishlearningcopilot.backend.dto.DashboardGrammarTrendResponse;
import com.englishlearningcopilot.backend.dto.DashboardSpeakingTrendResponse;
import com.englishlearningcopilot.backend.dto.DashboardStudyPlanResponse;
import com.englishlearningcopilot.backend.dto.DashboardWeeklyOverviewResponse;
import com.englishlearningcopilot.backend.dto.VocabularyLeaderboardItemResponse;
import com.englishlearningcopilot.backend.service.DashboardService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class DashboardControllerTest {

    @Mock
    private DashboardService dashboardService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new DashboardController(dashboardService)).build();
    }

    @Test
    void getCommunityLearningTrendsReturnsLeaderboardData() throws Exception {
        when(dashboardService.getCommunityLearningTrends())
                .thenReturn(new DashboardCommunityLearningTrendsResponse(
                        List.of(new DashboardSpeakingTrendResponse(1, "airport-checkin", "Airport Check-in", "Travel", 8)),
                        List.of(new VocabularyLeaderboardItemResponse(1, 10L, "accept", "starter", 12)),
                        List.of(new DashboardGrammarTrendResponse(1, "Tense", 6))
                ));

        mockMvc.perform(get("/api/dashboard/community-learning-trends"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.speaking[0].topic").value("Airport Check-in"))
                .andExpect(jsonPath("$.vocabulary[0].word").value("accept"))
                .andExpect(jsonPath("$.grammar[0].grammarCategory").value("Tense"));
    }

    @Test
    void getStudyPlanUsesPrincipalName() throws Exception {
        when(dashboardService.getStudyPlan("learner"))
                .thenReturn(new DashboardStudyPlanResponse(
                        new DailyPracticeProgressResponse(2, 5, 3, false),
                        new DailyPracticeProgressResponse(1, 3, 2, false),
                        4,
                        false
                ));

        mockMvc.perform(get("/api/dashboard/study-plan").principal(() -> "learner"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.streakDays").value(4))
                .andExpect(jsonPath("$.vocabulary.completed").value(2));

        verify(dashboardService).getStudyPlan("learner");
    }

    @Test
    void getWeeklyOverviewUsesPrincipalName() throws Exception {
        when(dashboardService.getWeeklyOverview("learner"))
                .thenReturn(new DashboardWeeklyOverviewResponse("8 min", "86 / 100", "3 days", "12", "5"));

        mockMvc.perform(get("/api/dashboard/weekly-overview").principal(() -> "learner"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.speakingDuration").value("8 min"))
                .andExpect(jsonPath("$.pronunciationReference").value("86 / 100"));

        verify(dashboardService).getWeeklyOverview("learner");
    }
}
