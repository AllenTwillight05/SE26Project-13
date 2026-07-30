package com.englishlearningcopilot.backend;

import static org.assertj.core.api.Assertions.assertThat;

import com.englishlearningcopilot.backend.service.dispatch.SpeakingTurnTaskService;
import com.englishlearningcopilot.backend.service.routing.SpeakingRouteSelector;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest(properties = "debug=false")
class SpeakingRoutingDisabledIntegrationTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void defaultSingleProviderModeDoesNotCreateFutureRoutingOrDispatchServices() {
        assertThat(applicationContext.getBeansOfType(SpeakingRouteSelector.class)).isEmpty();
        assertThat(applicationContext.getBeansOfType(SpeakingTurnTaskService.class)).isEmpty();
    }
}
