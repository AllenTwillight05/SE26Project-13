package com.englishlearningcopilot.backend.service.routing;

import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Disabled by default. These routes are deliberately not wired into the
 * current speaking request path until multi-provider scheduling is enabled.
 */
@ConfigurationProperties(prefix = "speaking.routing")
public class SpeakingRoutingProperties {

    private boolean enabled;
    private List<Route> routes = new ArrayList<>();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes == null ? new ArrayList<>() : new ArrayList<>(routes);
    }

    public static class Route {

        private String id;
        private SpeakingRouteCapability capability;
        private String endpoint;
        private String apiKey;
        private String model;
        private boolean enabled = true;
        private int maxConcurrent = 1;
        private int weight = 1;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public SpeakingRouteCapability getCapability() {
            return capability;
        }

        public void setCapability(SpeakingRouteCapability capability) {
            this.capability = capability;
        }

        public String getEndpoint() {
            return endpoint;
        }

        public void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }

        public String getApiKey() {
            return apiKey;
        }

        public void setApiKey(String apiKey) {
            this.apiKey = apiKey;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public int getMaxConcurrent() {
            return maxConcurrent;
        }

        public void setMaxConcurrent(int maxConcurrent) {
            this.maxConcurrent = maxConcurrent;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }
    }
}
