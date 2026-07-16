package com.englishlearningcopilot.backend.controller;

import com.englishlearningcopilot.backend.dto.DashboardCommunityLearningTrendsResponse;
import com.englishlearningcopilot.backend.service.DashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    /**
     * GET /api/dashboard/community-learning-trends
     * Get community learning leaderboards
    */
    @GetMapping("/community-learning-trends")
    public DashboardCommunityLearningTrendsResponse getCommunityLearningTrends() {
        return dashboardService.getCommunityLearningTrends();
    }
}
