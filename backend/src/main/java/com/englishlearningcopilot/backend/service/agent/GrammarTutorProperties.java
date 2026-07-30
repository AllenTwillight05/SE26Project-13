package com.englishlearningcopilot.backend.service.agent;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "grammar.tutor.sjtu")
public record GrammarTutorProperties(
        String endpoint,
        String apiKey,
        String model,
        double temperature,
        int maxTokens,
        int timeoutMs
) {

    public GrammarTutorProperties {
        if (endpoint == null || endpoint.isBlank()) {
            endpoint = "https://models.sjtu.edu.cn/api/v1";
        }
        endpoint = endpoint.replaceAll("/+$", "");
        if (model == null || model.isBlank()) {
            model = "deepseek-chat";
        }
        if (temperature <= 0) {
            temperature = 0.3;
        }
        if (maxTokens <= 0) {
            maxTokens = 500;
        }
        if (timeoutMs <= 0) {
            timeoutMs = 30000;
        }
    }

    public boolean hasApiKey() {
        return apiKey != null && !apiKey.isBlank();
    }
}
