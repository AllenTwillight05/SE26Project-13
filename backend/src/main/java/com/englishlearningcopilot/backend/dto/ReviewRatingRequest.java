package com.englishlearningcopilot.backend.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ReviewRatingRequest(
        @NotBlank String questionId,
        @Min(1) @Max(4) int rating
) {
}
