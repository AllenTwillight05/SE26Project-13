package com.englishlearningcopilot.backend.service.routing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;

class SpeakingRouteSelectorTest {

    private static final Instant NOW = Instant.parse("2026-07-28T08:00:00Z");

    @Test
    void allocatesLeastLoadedRouteAndHonorsConcurrentCapacity() {
        SpeakingRouteSelector selector = new SpeakingRouteSelector(properties(
                route("llm-a", SpeakingRouteCapability.LLM, 1, 1),
                route("llm-b", SpeakingRouteCapability.LLM, 2, 1)
        ));

        SpeakingRouteSelector.RouteLease first = selector.acquire(SpeakingRouteCapability.LLM, NOW);
        SpeakingRouteSelector.RouteLease second = selector.acquire(SpeakingRouteCapability.LLM, NOW);
        SpeakingRouteSelector.RouteLease third = selector.acquire(SpeakingRouteCapability.LLM, NOW);

        assertThat(first.route().getId()).isEqualTo("llm-a");
        assertThat(second.route().getId()).isEqualTo("llm-b");
        assertThat(third.route().getId()).isEqualTo("llm-b");
        assertThat(selector.snapshot("llm-b").inFlight()).isEqualTo(2);

        first.completeSuccessfully(NOW.plusSeconds(1));
        second.completeSuccessfully(NOW.plusSeconds(1));
        third.completeSuccessfully(NOW.plusSeconds(1));
    }

    @Test
    void rateLimitedRouteCoolsDownWithoutBlockingAnotherRoute() {
        SpeakingRouteSelector selector = new SpeakingRouteSelector(properties(
                route("llm-a", SpeakingRouteCapability.LLM, 1, 1),
                route("llm-b", SpeakingRouteCapability.LLM, 1, 1)
        ));

        SpeakingRouteSelector.RouteLease first = selector.acquire(SpeakingRouteCapability.LLM, NOW);
        assertThat(first.route().getId()).isEqualTo("llm-a");
        first.completeWithFailure(NOW.plusSeconds(1), SpeakingRouteFailure.RATE_LIMITED);

        SpeakingRouteSelector.RouteLease retry = selector.acquire(SpeakingRouteCapability.LLM, NOW.plusSeconds(2));
        assertThat(retry.route().getId()).isEqualTo("llm-b");
        assertThat(selector.snapshot("llm-a").cooldownUntil()).isAfter(NOW.plusSeconds(2));
        retry.completeSuccessfully(NOW.plusSeconds(3));
    }

    @Test
    void rejectsDisabledOrInvalidRoutingConfiguration() {
        SpeakingRoutingProperties disabled = new SpeakingRoutingProperties();
        assertThatThrownBy(() -> new SpeakingRouteSelector(disabled))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("must be enabled");

        SpeakingRoutingProperties invalid = properties(route("llm-a", SpeakingRouteCapability.LLM, 0, 1));
        assertThatThrownBy(() -> new SpeakingRouteSelector(invalid))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("at least one concurrent task");
    }

    private static SpeakingRoutingProperties properties(SpeakingRoutingProperties.Route... routes) {
        SpeakingRoutingProperties properties = new SpeakingRoutingProperties();
        properties.setEnabled(true);
        properties.setRoutes(List.of(routes));
        return properties;
    }

    private static SpeakingRoutingProperties.Route route(
            String id,
            SpeakingRouteCapability capability,
            int maxConcurrent,
            int weight
    ) {
        SpeakingRoutingProperties.Route route = new SpeakingRoutingProperties.Route();
        route.setId(id);
        route.setCapability(capability);
        route.setEndpoint("https://example.test/api/v1");
        route.setApiKey("test-key");
        route.setModel("test-model");
        route.setMaxConcurrent(maxConcurrent);
        route.setWeight(weight);
        return route;
    }
}
