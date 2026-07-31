package com.englishlearningcopilot.backend.config;

import com.englishlearningcopilot.backend.service.routing.SpeakingRouteSelector;
import com.englishlearningcopilot.backend.service.routing.SpeakingRoutingProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This configuration is intentionally absent in the default single-provider
 * mode, leaving the current speaking request path unchanged.
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = "speaking.routing.enabled", havingValue = "true")
@EnableConfigurationProperties(SpeakingRoutingProperties.class)
public class SpeakingRoutingConfiguration {

    @Bean
    public SpeakingRouteSelector speakingRouteSelector(SpeakingRoutingProperties properties) {
        return new SpeakingRouteSelector(properties);
    }
}
