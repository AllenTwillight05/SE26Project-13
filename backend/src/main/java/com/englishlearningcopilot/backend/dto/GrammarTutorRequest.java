package com.englishlearningcopilot.backend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;

public record GrammarTutorRequest(
        @NotNull Integer grammarQuestionId,
        @NotBlank
        @Pattern(regexp = "[A-E]", message = "selectedAnswer must be an option letter from A to E")
        String selectedAnswer,
        @NotBlank
        @Size(max = 1000)
        String message,
        @NotNull
        @Size(max = 8)
        List<@Valid GrammarTutorMessage> history
) {
}
