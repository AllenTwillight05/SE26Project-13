package com.englishlearningcopilot.backend.service.routing;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * In-memory route allocator for the future multi-provider mode. Callers obtain
 * a lease before invoking an external provider and must close it afterwards.
 * The class has no effect until the routing feature is explicitly enabled.
 */
public class SpeakingRouteSelector {

    private static final Duration RATE_LIMIT_COOLDOWN = Duration.ofSeconds(30);
    private static final Duration TRANSIENT_FAILURE_COOLDOWN = Duration.ofSeconds(5);
    private static final Duration PERMANENT_FAILURE_COOLDOWN = Duration.ofMinutes(5);

    private final Map<SpeakingRouteCapability, List<RouteState>> routesByCapability;

    public SpeakingRouteSelector(SpeakingRoutingProperties properties) {
        this.routesByCapability = validateAndBuild(properties);
    }

    /**
     * Reserves one route slot. It never retries or invokes a provider itself,
     * so future callers retain control of idempotency and response semantics.
     */
    public synchronized RouteLease acquire(SpeakingRouteCapability capability, Instant now) {
        List<RouteState> candidates = routesByCapability.get(capability);
        if (candidates == null || candidates.isEmpty()) {
            throw new NoAvailableSpeakingRouteException("No routes configured for " + capability + ".");
        }

        RouteState selected = candidates.stream()
                .filter(route -> route.isAvailable(now))
                .min(Comparator
                        .comparingDouble(RouteState::loadScore)
                        .thenComparingLong(RouteState::lastSelectedSequence)
                        .thenComparing(route -> route.route().getId()))
                .orElseThrow(() -> new NoAvailableSpeakingRouteException(
                        "All " + capability + " routes are at capacity or cooling down."
                ));

        selected.inFlight++;
        selected.lastSelectedSequence = nextSequence(candidates);
        return new RouteLease(this, selected, now);
    }

    public RouteSnapshot snapshot(String routeId) {
        synchronized (this) {
            return routesByCapability.values().stream()
                    .flatMap(List::stream)
                    .filter(route -> route.route().getId().equals(routeId))
                    .findFirst()
                    .map(RouteState::snapshot)
                    .orElseThrow(() -> new IllegalArgumentException("Unknown speaking route: " + routeId));
        }
    }

    private synchronized void release(RouteState state, Instant completedAt, Duration latency, SpeakingRouteFailure failure) {
        if (state.inFlight <= 0) {
            throw new IllegalStateException("Speaking route lease was released more than once.");
        }

        state.inFlight--;
        if (latency != null && !latency.isNegative()) {
            long elapsedMs = latency.toMillis();
            state.latencyEwmaMs = state.latencyEwmaMs == null
                    ? elapsedMs
                    : Math.round(state.latencyEwmaMs * 0.8 + elapsedMs * 0.2);
        }

        if (failure == null) {
            state.consecutiveFailures = 0;
            return;
        }

        state.consecutiveFailures++;
        state.cooldownUntil = completedAt.plus(cooldownFor(failure, state.consecutiveFailures));
    }

    private Duration cooldownFor(SpeakingRouteFailure failure, int consecutiveFailures) {
        Duration base = switch (failure) {
            case RATE_LIMITED -> RATE_LIMIT_COOLDOWN;
            case TRANSIENT -> TRANSIENT_FAILURE_COOLDOWN;
            case PERMANENT -> PERMANENT_FAILURE_COOLDOWN;
        };
        long multiplier = Math.min(4, Math.max(1, consecutiveFailures));
        return base.multipliedBy(multiplier);
    }

    private long nextSequence(List<RouteState> candidates) {
        return candidates.stream().mapToLong(RouteState::lastSelectedSequence).max().orElse(0) + 1;
    }

    private Map<SpeakingRouteCapability, List<RouteState>> validateAndBuild(SpeakingRoutingProperties properties) {
        if (properties == null || !properties.isEnabled()) {
            throw new IllegalArgumentException("Speaking routing must be enabled before creating a route selector.");
        }

        Map<SpeakingRouteCapability, List<RouteState>> result = new HashMap<>();
        Set<String> routeIds = new HashSet<>();
        for (SpeakingRoutingProperties.Route route : properties.getRoutes()) {
            validateRoute(route, routeIds);
            if (route.isEnabled()) {
                result.computeIfAbsent(route.getCapability(), ignored -> new ArrayList<>()).add(new RouteState(route));
            }
        }
        if (result.isEmpty()) {
            throw new IllegalArgumentException("Speaking routing is enabled but no enabled routes are configured.");
        }
        return result;
    }

    private void validateRoute(SpeakingRoutingProperties.Route route, Set<String> routeIds) {
        if (route == null || route.getId() == null || route.getId().isBlank()) {
            throw new IllegalArgumentException("Each speaking route requires a non-blank id.");
        }
        if (!routeIds.add(route.getId())) {
            throw new IllegalArgumentException("Speaking route ids must be unique: " + route.getId());
        }
        if (route.getCapability() == null) {
            throw new IllegalArgumentException("Speaking route " + route.getId() + " requires a capability.");
        }
        if (route.getMaxConcurrent() <= 0) {
            throw new IllegalArgumentException("Speaking route " + route.getId() + " must allow at least one concurrent task.");
        }
        if (route.getWeight() <= 0) {
            throw new IllegalArgumentException("Speaking route " + route.getId() + " must have a positive weight.");
        }
    }

    public static final class RouteLease implements AutoCloseable {

        private final SpeakingRouteSelector selector;
        private final RouteState state;
        private final Instant acquiredAt;
        private boolean released;

        private RouteLease(SpeakingRouteSelector selector, RouteState state, Instant acquiredAt) {
            this.selector = selector;
            this.state = state;
            this.acquiredAt = acquiredAt;
        }

        public SpeakingRoutingProperties.Route route() {
            return state.route();
        }

        public void completeSuccessfully(Instant completedAt) {
            release(completedAt, null);
        }

        public void completeWithFailure(Instant completedAt, SpeakingRouteFailure failure) {
            if (failure == null) {
                throw new IllegalArgumentException("A failed route lease requires a failure type.");
            }
            release(completedAt, failure);
        }

        @Override
        public void close() {
            completeSuccessfully(Instant.now());
        }

        private void release(Instant completedAt, SpeakingRouteFailure failure) {
            if (released) {
                throw new IllegalStateException("Speaking route lease was released more than once.");
            }
            released = true;
            selector.release(state, completedAt, Duration.between(acquiredAt, completedAt), failure);
        }
    }

    public record RouteSnapshot(
            String routeId,
            SpeakingRouteCapability capability,
            int inFlight,
            Long latencyEwmaMs,
            int consecutiveFailures,
            Instant cooldownUntil
    ) {
    }

    private static final class RouteState {

        private final SpeakingRoutingProperties.Route route;
        private int inFlight;
        private Long latencyEwmaMs;
        private int consecutiveFailures;
        private Instant cooldownUntil;
        private long lastSelectedSequence;

        private RouteState(SpeakingRoutingProperties.Route route) {
            this.route = route;
        }

        private SpeakingRoutingProperties.Route route() {
            return route;
        }

        private boolean isAvailable(Instant now) {
            return inFlight < route.getMaxConcurrent()
                    && (cooldownUntil == null || !cooldownUntil.isAfter(now));
        }

        private double loadScore() {
            return (double) inFlight / route.getWeight();
        }

        private long lastSelectedSequence() {
            return lastSelectedSequence;
        }

        private RouteSnapshot snapshot() {
            return new RouteSnapshot(
                    route.getId(),
                    route.getCapability(),
                    inFlight,
                    latencyEwmaMs,
                    consecutiveFailures,
                    cooldownUntil
            );
        }
    }

    public static class NoAvailableSpeakingRouteException extends RuntimeException {

        public NoAvailableSpeakingRouteException(String message) {
            super(message);
        }
    }
}
