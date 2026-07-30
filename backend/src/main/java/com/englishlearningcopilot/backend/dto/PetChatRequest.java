package com.englishlearningcopilot.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PetChatRequest(
        @NotBlank
        @Size(max = 300)
        String message
) {
}
