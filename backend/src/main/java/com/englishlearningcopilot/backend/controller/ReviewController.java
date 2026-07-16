package com.englishlearningcopilot.backend.controller;

import com.englishlearningcopilot.backend.dto.DueVocabularyCard;
import com.englishlearningcopilot.backend.dto.ReviewRatingRequest;
import com.englishlearningcopilot.backend.service.ReviewService;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/vocabulary/review-vocabulary")
    public List<DueVocabularyCard> getDueVocabulary(Principal principal) {
        Long userId = reviewService.getUserIdByUsername(principal.getName());
        return reviewService.getDueVocabulary(userId);
    }

    @PostMapping("/vocabulary/review")
    public Map<String, Boolean> submitRating(
            Principal principal,
            @Valid @RequestBody ReviewRatingRequest request
    ) {
        Long userId = reviewService.getUserIdByUsername(principal.getName());
        reviewService.submitRating(userId, request.questionId(), request.rating());
        return Map.of("ok", true);
    }
}
